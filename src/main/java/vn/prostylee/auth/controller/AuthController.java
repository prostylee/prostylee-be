package vn.prostylee.auth.controller;

import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.auth.dto.request.*;
import vn.prostylee.auth.dto.response.JwtAuthenticationToken;
import vn.prostylee.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = ApiVersion.API_V1 + "/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-in")
    @ResponseStatus(code = HttpStatus.OK)
    public JwtAuthenticationToken login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
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

    @PutMapping("/change-password")
    @ResponseStatus(code = HttpStatus.OK)
    public JwtAuthenticationToken changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return authService.changePassword(request);
    }
}
