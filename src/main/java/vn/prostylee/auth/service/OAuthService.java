package vn.prostylee.auth.service;

import vn.prostylee.auth.dto.request.OAuthRequest;
import vn.prostylee.core.dto.response.SimpleResponse;

public interface OAuthService {

    SimpleResponse signUp(OAuthRequest oAuthRequest);
}
