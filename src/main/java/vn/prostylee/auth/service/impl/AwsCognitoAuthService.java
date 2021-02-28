package vn.prostylee.auth.service.impl;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.configure.properties.AwsCognitoProperties;
import vn.prostylee.auth.dto.request.ChangePasswordRequest;
import vn.prostylee.auth.dto.request.ForgotPasswordRequest;
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

    private final AWSCognitoIdentityProvider cognitoClient;

    @Override
    public JwtAuthenticationToken login(LoginRequest request) {
        log.info("LoginRequest {}", request);
        final Map<String, String> authParams = new HashMap<>();
        authParams.put(USERNAME, request.getUsername());
        authParams.put(PASS_WORD, request.getPassword());

        final AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest();
        authRequest.withAuthFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
                .withClientId(awsCognitoProperties.getClientId())
                .withUserPoolId(awsCognitoProperties.getPoolId())
                .withAuthParameters(authParams);

        AdminInitiateAuthResult result = cognitoClient.adminInitiateAuth(authRequest);
        log.info("AdminInitiateAuthResult {}", result);
        log.info("AdminInitiateAuthResult.getAuthenticationResult {}", result.getAuthenticationResult());
        return JwtAuthenticationToken.builder()
                .accessToken(result.getAuthenticationResult().getAccessToken())
                .refreshToken(result.getAuthenticationResult().getRefreshToken())
                .build();
    }

    @Override
    public JwtAuthenticationToken register(RegisterRequest request) {
        AdminCreateUserRequest cognitoRequest = new AdminCreateUserRequest()
                .withUserPoolId(awsCognitoProperties.getPoolId())
                .withUsername(request.getUsername())
                .withUserAttributes(
                        new AttributeType()
                                .withValue(request.getUsername()),
                        new AttributeType()
                                .withName("name")
                                .withValue(request.getFullName()),
//                new AttributeType()
//                        .withName("name")
//                        .withValue(request.getName()),
//                new AttributeType()
//                        .withName("family_name")
//                        .withValue(request.getLastname()),
//                new AttributeType()
//                        .withName("phone_number")
//                        .withValue(request.getPhoneNumber()),
//                new AttributeType()
//                        .withName("custom:companyName")
//                        .withValue(request.getCompanyName()),
//                new AttributeType()
//                        .withName("custom:companyPosition")
//                        .withValue(request.getCompanyPosition()),
                        new AttributeType()
                                .withName("email_verified")
                                .withValue("true"))
                .withTemporaryPassword("TEMPORARY_PASSWORD")
                .withMessageAction("SUPPRESS")
                .withDesiredDeliveryMediums(DeliveryMediumType.EMAIL)
                .withForceAliasCreation(Boolean.FALSE);

        AdminCreateUserResult createUserResult =  cognitoClient.adminCreateUser(cognitoRequest);
        log.info("createUserResult {}", createUserResult);

        UserType cognitoUser =  createUserResult.getUser();
        log.info("cognitoUser {}", cognitoUser);

        return JwtAuthenticationToken.builder()
                .build();
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
        com.amazonaws.services.cognitoidp.model.ChangePasswordRequest changePasswordRequest= new com.amazonaws.services.cognitoidp.model.ChangePasswordRequest()
//                .withAccessToken(request.getAccessToken()) // TODO
                .withPreviousPassword(request.getPassword())
                .withProposedPassword(request.getPassword());

        cognitoClient.changePassword(changePasswordRequest);
        return null;
    }

    @Override
    public JwtAuthenticationToken loginWithSocial(LoginSocialRequest request) {
        return null;
    }
}
