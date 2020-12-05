package vn.prostylee.auth.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import vn.prostylee.auth.dto.AuthUserDetails;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.auth.dto.request.*;
import vn.prostylee.auth.dto.response.JwtAuthenticationToken;
import vn.prostylee.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public JwtAuthenticationToken loginWithSocial(@Valid @RequestBody LoginSocialRequest request) throws FirebaseAuthException {
        FirebaseToken fireBaseToken = FirebaseAuth.getInstance().verifyIdToken(request.getIdToken());

        if(!StringUtils.isEmpty(fireBaseToken.getEmail())){
            return authService.loginWithSocial(fireBaseToken);
        }
        return new JwtAuthenticationToken();
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
