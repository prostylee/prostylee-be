package vn.prostylee.auth.service.impl;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;
import vn.prostylee.auth.configure.properties.SecurityProperties;
import vn.prostylee.auth.constant.AuthConstants;
import vn.prostylee.auth.constant.AuthRole;
import vn.prostylee.auth.constant.Scope;
import vn.prostylee.auth.constant.SocialProviderType;
import vn.prostylee.auth.dto.AuthUserDetails;
import vn.prostylee.auth.dto.request.*;
import vn.prostylee.auth.dto.response.UserTempResponse;
import vn.prostylee.auth.dto.response.JwtAuthenticationToken;
import vn.prostylee.auth.dto.response.ZaloResponse;
import vn.prostylee.auth.entity.Feature;
import vn.prostylee.auth.entity.User;
import vn.prostylee.auth.entity.UserLinkAccount;
import vn.prostylee.auth.exception.AuthenticationException;
import vn.prostylee.auth.exception.InvalidJwtToken;
import vn.prostylee.auth.repository.UserRepository;
import vn.prostylee.auth.service.UserLinkAccountService;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.auth.service.UserTempService;
import vn.prostylee.auth.service.AuthService;
import vn.prostylee.auth.token.AccessToken;
import vn.prostylee.auth.token.factory.JwtTokenFactory;
import vn.prostylee.auth.token.parser.TokenParser;
import vn.prostylee.auth.token.verifier.TokenVerifier;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.core.utils.EncrytedPasswordUtils;
import vn.prostylee.notification.constant.EmailTemplateType;
import vn.prostylee.notification.dto.mail.MailInfo;
import vn.prostylee.notification.dto.mail.MailTemplateConfig;
import vn.prostylee.notification.dto.response.EmailTemplateResponse;
import vn.prostylee.notification.service.EmailService;
import vn.prostylee.notification.service.EmailTemplateService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenFactory tokenFactory;

    @Autowired
    private TokenVerifier tokenVerifier;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private TokenParser tokenParser;

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    private UserLinkAccountService userLinkAccountService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailTemplateService emailTemplateService;

    @Autowired
    private UserTempService userTempService;

    @Autowired
    private AuthenticatedProvider authenticatedProvider;

    @Override
    public JwtAuthenticationToken login(LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return this.createResponse((AuthUserDetails) auth.getPrincipal());
    }

    @Override
    public JwtAuthenticationToken loginWithSocial(LoginSocialRequest request) throws FirebaseAuthException {
        if(SocialProviderType.FIREBASE == request.getProviderType()) {
            FirebaseToken fireBaseToken = FirebaseAuth.getInstance().verifyIdToken(request.getIdToken());
            if(ObjectUtils.isNotEmpty(fireBaseToken)){
                Optional<UserLinkAccount> linkAccount = userLinkAccountService.getUserLinkAccountBy(fireBaseToken);
                if(linkAccount.isPresent()) {
                    return processExist(linkAccount);
                } else {
                    return processNew(fireBaseToken);
                }
            }
        } else if (SocialProviderType.ZALO == request.getProviderType()) {
            TcpClient tcpClient = getTcpClient();
            WebClient client = getWebClient(tcpClient);
            call(client);
        } else {
            throw new AuthenticationException("Provider type incorrect or doesn't support");
        }
        return new JwtAuthenticationToken();
    }

    private Mono<ZaloResponse> call(WebClient client) {
        return  client.method(HttpMethod.GET).uri(uriBuilder -> uriBuilder
                .path("/{version}/me?access_token={token}&fields={fields}")
                .build("v2.0", "sjdkajkdjToken gui tu client len", "id,birthday,name,gender,picture" ))
                .retrieve().bodyToMono(ZaloResponse.class);
    }

    private WebClient getWebClient(TcpClient tcpClient) {
        WebClient client = WebClient
                .builder()
                .baseUrl("https://graph.zalo.me")
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        return client;
    }

    private TcpClient getTcpClient() {
        return TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                });
    }

    private JwtAuthenticationToken processNew(FirebaseToken firebaseToken) {
        User user = userService.save(firebaseToken);
        AuthUserDetails authUserDetails = new AuthUserDetails(user, null);
        return this.createResponse(authUserDetails);
    }

    private JwtAuthenticationToken processExist(Optional<UserLinkAccount> linkAccount) {
        User user = linkAccount.get().getUser();
        AuthUserDetails authUserDetails = new AuthUserDetails(user, null);
        return this.createResponse(authUserDetails);
    }

    @Override
    public JwtAuthenticationToken register(RegisterRequest registerRequest) {
        UserRequest userRequest = BeanUtil.copyProperties(registerRequest, UserRequest.class);
        userRequest.setRoles(Collections.singletonList(AuthRole.BUYER.name()));
        userRequest.setActive(registerRequest.getActive() == null ? Boolean.TRUE : registerRequest.getActive());
        userRequest.setEmail(registerRequest.getUsername());
        userService.save(userRequest);

        this.sendEmailWelcome(userRequest.getEmail(), registerRequest);

        LoginRequest loginRequest = LoginRequest.builder()
                .username(registerRequest.getUsername())
                .password(registerRequest.getPassword())
                .build();
        return login(loginRequest);
    }

    private void sendEmailWelcome(String email, RegisterRequest registerRequest) {
        try {
            EmailTemplateResponse emailTemplateResponse = emailTemplateService.findByType(EmailTemplateType.WELCOME.name());
            MailTemplateConfig config = MailTemplateConfig.builder()
                    .mailContent(emailTemplateResponse.getContent())
                    .mailSubject(emailTemplateResponse.getTitle())
                    .mailIsHtml(true)
                    .build();

            MailInfo mailInfo = new MailInfo();
            mailInfo.addTo(email);
            emailService.sendAsync(mailInfo, config, registerRequest);
        } catch(ResourceNotFoundException e) {
            log.warn("There is no email template for sending a welcome email to a new user");
        }
    }

    private JwtAuthenticationToken createResponse(AuthUserDetails userDetail) {
        AccessToken accessToken = tokenFactory.createAccessToken(userDetail);
        JwtAuthenticationToken token = JwtAuthenticationToken.builder()
                .accessToken(accessToken.getToken())
                .refreshToken(tokenFactory.createRefreshToken(userDetail).getToken())
                .tokenType(AuthConstants.BEARER_PREFIX)
                .build();
        return token;
    }

    @Override
    public JwtAuthenticationToken refreshToken(RefreshTokenRequest request) {
        if (tokenVerifier.verify(request.getRefreshToken()) && checkRefreshTokenScope(request.getRefreshToken())) {
            Long userId = tokenParser.getUserIdFromJWT(request.getRefreshToken(), securityProperties.getJwt().getTokenSigningKey());
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User is not exists by getting with id " + userId));
            AuthUserDetails userDetail = new AuthUserDetails(user, this.getFeatures(user));
            AccessToken accessToken = tokenFactory.createAccessToken(userDetail);

            return JwtAuthenticationToken.builder()
                    .accessToken(accessToken.getToken())
                    .refreshToken(request.getRefreshToken())
                    .tokenType(AuthConstants.BEARER_PREFIX)
                    .build();
        }
        throw new InvalidJwtToken("The refresh token is invalid");
    }

    @Override
    public boolean forgotPassword(ForgotPasswordRequest request) {
        userRepository.findActivatedUserByEmail(request.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException("User is not exists by getting with email " + request.getEmail()));

        EmailTemplateResponse emailTemplateResponse = emailTemplateService.findByType(EmailTemplateType.FORGOT_PASSWORD.name());
        MailTemplateConfig config = MailTemplateConfig.builder()
                .mailContent(emailTemplateResponse.getContent())
                .mailSubject(emailTemplateResponse.getTitle())
                .mailIsHtml(true)
                .build();

        UserTempResponse userTempResponse = userTempService.createUserTemp(request.getEmail());

        MailInfo mailInfo = new MailInfo();
        mailInfo.addTo(request.getEmail());
        emailService.sendAsync(mailInfo, config, userTempResponse);
        return true;
    }

    @Override
    public JwtAuthenticationToken changePassword(ChangePasswordRequest request) {
        String email = request.getEmail();
        final User user = userRepository.findActivatedUserByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User is not exists by getting with email " + email));

        if ((authenticatedProvider.getUserId().isPresent() && EncrytedPasswordUtils.isMatched(request.getPassword(), user.getPassword())) ||
                userTempService.isValid(email, request.getPassword())) {
            // Save a new password
            user.setPassword(EncrytedPasswordUtils.encryptPassword(request.getNewPassword()));
            userRepository.save(user);

            // Delete temp account
            userTempService.delete(email);

            // Return token
            LoginRequest loginRequest = LoginRequest.builder()
                    .username(email)
                    .password(request.getNewPassword())
                    .build();
            return login(loginRequest);
        }
        throw new ResourceNotFoundException("Incorrect password or password is expired");
    }

    private List<Feature> getFeatures(User user) {
        List<Feature> features = new ArrayList<>();
        user.getRoles().forEach(role -> features.addAll(role.getFeatures()));
        return features;
    }

    private boolean checkRefreshTokenScope(String refreshToken) {
        Jws<Claims> claimsJws = tokenParser.parseClaims(refreshToken, securityProperties.getJwt().getTokenSigningKey());
        Claims claims = claimsJws.getBody();
        List<String> scopes = (List<String>) claims.get(JwtTokenFactory.TOKEN_SCOPES_KEY);
        if (CollectionUtils.isEmpty(scopes)) {
            throw new InvalidJwtToken("The refresh token is invalid scope");
        }
        for (String scope : scopes) {
            if(Scope.REFRESH_TOKEN.authority().equals(scope)) {
                return true;
            }
        }
        return false;
    }
}
