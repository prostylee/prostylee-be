package vn.prostylee.auth.service;

import vn.prostylee.auth.dto.request.*;
import vn.prostylee.auth.dto.response.JwtAuthenticationToken;

public interface AuthService {

    JwtAuthenticationToken login(LoginRequest request);

    JwtAuthenticationToken register(RegisterRequest request);

    JwtAuthenticationToken refreshToken(RefreshTokenRequest request);

    boolean forgotPassword(ForgotPasswordRequest request);

    JwtAuthenticationToken changePassword(ChangePasswordRequest request);

}
