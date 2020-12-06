package vn.prostylee.auth.service;

import vn.prostylee.auth.constant.SocialProviderType;
import vn.prostylee.auth.dto.request.LoginSocialRequest;
import vn.prostylee.auth.dto.response.JwtAuthenticationToken;

public interface AuthenticationService {

    JwtAuthenticationToken login(LoginSocialRequest request);

    SocialProviderType getProviderType();
}
