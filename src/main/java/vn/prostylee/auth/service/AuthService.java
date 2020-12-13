package vn.prostylee.auth.service;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import vn.prostylee.auth.dto.AuthUserDetails;
import vn.prostylee.auth.dto.request.*;
import vn.prostylee.auth.dto.response.JwtAuthenticationToken;

public interface AuthService {

    JwtAuthenticationToken login(LoginRequest request);

    JwtAuthenticationToken register(RegisterRequest request);

    JwtAuthenticationToken refreshToken(RefreshTokenRequest request);

    boolean forgotPassword(ForgotPasswordRequest request);

    JwtAuthenticationToken changePassword(ChangePasswordRequest request);

    JwtAuthenticationToken loginWithSocial(LoginSocialRequest request);
}
