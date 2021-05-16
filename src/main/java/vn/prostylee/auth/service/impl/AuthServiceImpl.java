package vn.prostylee.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.constant.AuthRole;
import vn.prostylee.auth.dto.AuthUserDetails;
import vn.prostylee.auth.dto.request.*;
import vn.prostylee.auth.dto.response.JwtAuthenticationToken;
import vn.prostylee.auth.exception.AuthenticationException;
import vn.prostylee.auth.service.*;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.notification.event.email.EmailEvent;
import vn.prostylee.notification.event.email.EmailEventDto;
import vn.prostylee.notification.constant.EmailTemplateType;

import java.util.Collections;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final UserAuthTokenService userAuthTokenService;

    private final UserPasswordService userPasswordService;

    private final UserService userService;

    private final AuthServiceFactory authServiceFactory;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public JwtAuthenticationToken login(LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return userAuthTokenService.createToken((AuthUserDetails) auth.getPrincipal());
    }

    @Override
    public JwtAuthenticationToken loginWithSocial(LoginSocialRequest request) {
        SocialAuthService socialAuthService = authServiceFactory.getService(request.getProviderType());
        AuthUserDetails authUserDetails = socialAuthService.login(request);
        return userAuthTokenService.createToken(authUserDetails);
    }

    @Override
    public JwtAuthenticationToken register(RegisterRequest registerRequest) {
        UserRequest userRequest = BeanUtil.copyProperties(registerRequest, UserRequest.class);
        userRequest.setRoles(Collections.singletonList(AuthRole.BUYER.name()));
        userRequest.setActive(registerRequest.getActive() == null ? Boolean.TRUE : registerRequest.getActive());
        userService.save(userRequest);

        this.sendEmailWelcome(userRequest.getEmail(), userRequest);

        LoginRequest loginRequest = LoginRequest.builder()
                .username(registerRequest.getUsername())
                .password(registerRequest.getPassword())
                .build();
        return login(loginRequest);
    }

    @Override
    public JwtAuthenticationToken refreshToken(RefreshTokenRequest request) {
        return userAuthTokenService.refreshToken(request);
    }

    @Override
    public boolean forgotPassword(ForgotPasswordRequest request) {
        return userPasswordService.forgotPassword(request);
    }

    @Override
    public boolean verifyOtp(OtpVerificationRequest request) {
        return userPasswordService.verifyOtp(request);
    }

    @Override
    public JwtAuthenticationToken changePassword(ChangePasswordRequest request) {
        boolean isSuccess = userPasswordService.changePassword(request);
        if (isSuccess) {
            LoginRequest loginRequest = LoginRequest.builder()
                    .username(request.getEmail())
                    .password(request.getNewPassword())
                    .build();
            return login(loginRequest);
        }
        throw new AuthenticationException("Can not change the password");
    }

    private void sendEmailWelcome(String email, UserRequest userRequest) {
        EmailEventDto<?> eventDto = EmailEventDto.builder()
                .emailTemplateType(EmailTemplateType.WELCOME)
                .emails(Collections.singletonList(email))
                .data(userRequest)
                .build();
        eventPublisher.publishEvent(new EmailEvent(eventDto));
    }
}
