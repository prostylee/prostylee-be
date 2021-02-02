package vn.prostylee.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.prostylee.auth.dto.request.*;
import vn.prostylee.auth.dto.response.JwtAuthenticationToken;
import vn.prostylee.auth.service.AuthService;
import vn.prostylee.core.constant.ApiVersion;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = ApiVersion.API_V1 + "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-in")
    @ResponseStatus(code = HttpStatus.OK)
    public JwtAuthenticationToken login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/sign-in-with-social")
    @ResponseStatus(code = HttpStatus.OK)
    public JwtAuthenticationToken loginWithSocial(@Valid @RequestBody LoginSocialRequest request) {
        return authService.loginWithSocial(request);
    }

    @PostMapping("/sign-up")
    @ResponseStatus(code = HttpStatus.OK)
    public JwtAuthenticationToken register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/refresh")
    @ResponseStatus(code = HttpStatus.OK)
    public JwtAuthenticationToken refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refreshToken(request);
    }

    @PostMapping("/forgot-password")
    @ResponseStatus(code = HttpStatus.OK)
    public Boolean forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return authService.forgotPassword(request);
    }

    @PostMapping("/verify-otp")
    @ResponseStatus(code = HttpStatus.OK)
    public Boolean verifyOtp(@Valid @RequestBody OtpVerificationRequest request) {
        return authService.verifyOtp(request);
    }

    @PutMapping("/change-password")
    @ResponseStatus(code = HttpStatus.OK)
    public JwtAuthenticationToken changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return authService.changePassword(request);
    }
}
