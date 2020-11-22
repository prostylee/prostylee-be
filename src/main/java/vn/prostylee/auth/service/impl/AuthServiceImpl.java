package vn.prostylee.auth.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.prostylee.auth.configure.properties.SecurityProperties;
import vn.prostylee.auth.constant.Auth;
import vn.prostylee.auth.constant.AuthRole;
import vn.prostylee.auth.constant.Scope;
import vn.prostylee.auth.dto.AuthUserDetails;
import vn.prostylee.auth.dto.request.*;
import vn.prostylee.auth.dto.response.AccountTempResponse;
import vn.prostylee.auth.dto.response.JwtAuthenticationToken;
import vn.prostylee.auth.entity.Account;
import vn.prostylee.auth.entity.Feature;
import vn.prostylee.auth.exception.InvalidJwtToken;
import vn.prostylee.auth.repository.custom.CustomAccountRepository;
import vn.prostylee.auth.service.AccountService;
import vn.prostylee.auth.service.AccountTempService;
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
    @Qualifier("customAccountRepository")
    private CustomAccountRepository userRepository;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private TokenParser tokenParser;

    @Autowired
    @Qualifier("accountService")
    private AccountService accountService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailTemplateService emailTemplateService;

    @Autowired
    private AccountTempService accountTempService;

    @Autowired
    private AuthenticatedProvider authenticatedProvider;

    @Override
    public JwtAuthenticationToken login(LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(auth);
        return this.createResponse((AuthUserDetails) auth.getPrincipal());
    }

    @Override
    public JwtAuthenticationToken register(RegisterRequest registerRequest) {
        AccountRequest accountRequest = BeanUtil.copyProperties(registerRequest, AccountRequest.class);
        accountRequest.setRoles(Collections.singletonList(AuthRole.BUYER.name()));
        accountRequest.setActive(registerRequest.getActive() == null ? Boolean.TRUE : registerRequest.getActive());
        accountRequest.setEmail(registerRequest.getUsername());
        accountService.save(accountRequest);

        this.sendEmailWelcome(accountRequest.getEmail(), registerRequest);

        LoginRequest loginRequest = LoginRequest.builder()
                .username(registerRequest.getUsername())
                .password(registerRequest.getPassword())
                .build();
        return login(loginRequest);
    }

    private void sendEmailWelcome(String email, RegisterRequest registerRequest) {
        try {
            EmailTemplateResponse emailTemplateResponse = emailTemplateService.findByTypeAndLanguage(EmailTemplateType.WELCOME.name(), registerRequest.getLanguage());
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
        return JwtAuthenticationToken.builder()
                .accessToken(accessToken.getToken())
                .refreshToken(tokenFactory.createRefreshToken(userDetail).getToken())
                .tokenType(Auth.BEARER_PREFIX)
                .build();
    }

    @Override
    public JwtAuthenticationToken refreshToken(RefreshTokenRequest request) {
        if (tokenVerifier.verify(request.getRefreshToken()) && checkRefreshTokenScope(request.getRefreshToken())) {
            Long userId = tokenParser.getUserIdFromJWT(request.getRefreshToken(), securityProperties.getJwt().getTokenSigningKey());
            Account account = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User is not exists by getting with id " + userId));
            AuthUserDetails userDetail = new AuthUserDetails(account, this.getFeatures(account));
            AccessToken accessToken = tokenFactory.createAccessToken(userDetail);

            return JwtAuthenticationToken.builder()
                    .accessToken(accessToken.getToken())
                    .refreshToken(request.getRefreshToken())
                    .tokenType(Auth.BEARER_PREFIX)
                    .build();
        }
        throw new InvalidJwtToken("The refresh token is invalid");
    }

    @Override
    public boolean forgotPassword(ForgotPasswordRequest request) {
        userRepository.findActivatedUserByEmail(request.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException("User is not exists by getting with email " + request.getEmail()));

        EmailTemplateResponse emailTemplateResponse = emailTemplateService.findByTypeAndLanguage(EmailTemplateType.FORGOT_PASSWORD.name(), request.getLanguage());
        MailTemplateConfig config = MailTemplateConfig.builder()
                .mailContent(emailTemplateResponse.getContent())
                .mailSubject(emailTemplateResponse.getTitle())
                .mailIsHtml(true)
                .build();

        AccountTempResponse accountTempResponse = accountTempService.createAccountTemp(request.getEmail());

        MailInfo mailInfo = new MailInfo();
        mailInfo.addTo(request.getEmail());
        emailService.sendAsync(mailInfo, config, accountTempResponse);
        return true;
    }

    @Override
    public JwtAuthenticationToken changePassword(ChangePasswordRequest request) {
        String email = request.getEmail();
        final Account account = userRepository.findActivatedUserByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User is not exists by getting with email " + email));

        if ((authenticatedProvider.getUserId().isPresent() && EncrytedPasswordUtils.isMatched(request.getPassword(), account.getPassword())) ||
                accountTempService.isValid(email, request.getPassword())) {
            // Save a new password
            account.setPassword(EncrytedPasswordUtils.encryptPassword(request.getNewPassword()));
            userRepository.save(account);

            // Delete temp account
            accountTempService.delete(email);

            // Return token
            LoginRequest loginRequest = LoginRequest.builder()
                    .username(email)
                    .password(request.getNewPassword())
                    .build();
            return login(loginRequest);
        }
        throw new ResourceNotFoundException("Incorrect password or password is expired");
    }

    private List<Feature> getFeatures(Account account) {
        List<Feature> features = new ArrayList<>();
        account.getRoles().forEach(role -> features.addAll(role.getFeatures()));
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
