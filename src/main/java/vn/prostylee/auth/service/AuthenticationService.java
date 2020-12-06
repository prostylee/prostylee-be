package vn.prostylee.auth.service;

import com.google.firebase.auth.FirebaseAuthException;
import vn.prostylee.auth.constant.SocialProviderType;
import vn.prostylee.auth.dto.request.LoginSocialRequest;
import vn.prostylee.auth.dto.response.JwtAuthenticationToken;

public interface AuthenticationService {
    JwtAuthenticationToken login(LoginSocialRequest request);
    SocialProviderType getProviderType();
}
