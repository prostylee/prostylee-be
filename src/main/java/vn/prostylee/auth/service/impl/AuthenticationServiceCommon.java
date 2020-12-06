package vn.prostylee.auth.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import vn.prostylee.auth.configure.properties.SecurityProperties;
import vn.prostylee.auth.constant.AuthConstants;
import vn.prostylee.auth.constant.Scope;
import vn.prostylee.auth.dto.AuthUserDetails;
import vn.prostylee.auth.dto.request.RegisterRequest;
import vn.prostylee.auth.dto.response.JwtAuthenticationToken;
import vn.prostylee.auth.entity.Feature;
import vn.prostylee.auth.entity.User;
import vn.prostylee.auth.exception.InvalidJwtToken;
import vn.prostylee.auth.token.AccessToken;
import vn.prostylee.auth.token.factory.JwtTokenFactory;
import vn.prostylee.auth.token.parser.TokenParser;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.notification.constant.EmailTemplateType;
import vn.prostylee.notification.dto.mail.MailInfo;
import vn.prostylee.notification.dto.mail.MailTemplateConfig;
import vn.prostylee.notification.dto.response.EmailTemplateResponse;
import vn.prostylee.notification.service.EmailService;
import vn.prostylee.notification.service.EmailTemplateService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AuthenticationServiceCommon {
    @Autowired
    private JwtTokenFactory tokenFactory;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailTemplateService emailTemplateService;

    @Autowired
    private TokenParser tokenParser;

    @Autowired
    private SecurityProperties securityProperties;

    protected JwtAuthenticationToken createResponse(AuthUserDetails userDetail) {
        AccessToken accessToken = tokenFactory.createAccessToken(userDetail);
        JwtAuthenticationToken token = JwtAuthenticationToken.builder()
                .accessToken(accessToken.getToken())
                .refreshToken(tokenFactory.createRefreshToken(userDetail).getToken())
                .tokenType(AuthConstants.BEARER_PREFIX)
                .build();
        return token;
    }

    protected void sendEmailWelcome(String email, RegisterRequest registerRequest) {
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

    protected boolean checkRefreshTokenScope(String refreshToken) {
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

    protected List<Feature> getFeatures(User user) {
        List<Feature> features = new ArrayList<>();
        user.getRoles().forEach(role -> features.addAll(role.getFeatures()));
        return features;
    }

}
