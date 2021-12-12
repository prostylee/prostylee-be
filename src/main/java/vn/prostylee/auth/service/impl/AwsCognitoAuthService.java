package vn.prostylee.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import vn.prostylee.auth.configure.properties.AwsCognitoProperties;
import vn.prostylee.auth.dto.request.*;
import vn.prostylee.auth.dto.response.JwtAuthenticationToken;
import vn.prostylee.auth.service.AuthService;

import java.util.HashMap;
import java.util.Map;

// TODO Implement full auth feature with AWS Cognito
@Slf4j
@Service
@Qualifier("awsCognitoAuthService")
@RequiredArgsConstructor
public class AwsCognitoAuthService implements AuthService {
    private static final String PASS_WORD = "PASSWORD";
    private static final String USERNAME = "USERNAME";

    private final AwsCognitoProperties awsCognitoProperties;

    private final CognitoIdentityProviderClient cognitoClient;

    @Override
    public JwtAuthenticationToken login(LoginRequest request) {
        log.info("LoginRequest {}", request);
        final Map<String, String> authParams = new HashMap<>();
        authParams.put(USERNAME, request.getUsername());
        authParams.put(PASS_WORD, request.getPassword());

        final AdminInitiateAuthRequest authRequest = AdminInitiateAuthRequest.builder()
                .authFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
                .clientId(awsCognitoProperties.getClientId())
                .userPoolId(awsCognitoProperties.getPoolId())
                .authParameters(authParams)
                .build();

        AdminInitiateAuthResponse result = cognitoClient.adminInitiateAuth(authRequest);
        log.info("AdminInitiateAuthResult {}", result.authenticationResult());
        return JwtAuthenticationToken.builder()
                .accessToken(result.authenticationResult().accessToken())
                .refreshToken(result.authenticationResult().refreshToken())
                .build();
    }

    @Override
    public JwtAuthenticationToken register(RegisterRequest request) {
        // Refer https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/cognito/src/main/java/com/example/cognito/SignUpUser.java

        throw new UnsupportedOperationException("Register feature is not supported yet}");
    }

    @Override
    public JwtAuthenticationToken refreshToken(RefreshTokenRequest request) {
        return null;
    }

    @Override
    public boolean forgotPassword(ForgotPasswordRequest request) {
        return false;
    }

    @Override
    public boolean verifyOtp(OtpVerificationRequest request) {
        return false;
    }

    @Override
    public JwtAuthenticationToken changePassword(ChangePasswordRequest request) {
        throw new UnsupportedOperationException("Change password feature is not supported yet}");
    }

    @Override
    public JwtAuthenticationToken loginWithSocial(LoginSocialRequest request) {
        return null;
    }
}
