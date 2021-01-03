package vn.prostylee.auth.service;

import vn.prostylee.auth.dto.AuthUserDetails;
import vn.prostylee.auth.dto.request.RefreshTokenRequest;
import vn.prostylee.auth.dto.response.JwtAuthenticationToken;

public interface UserAuthTokenService {

    JwtAuthenticationToken createToken(AuthUserDetails userDetail);

    JwtAuthenticationToken refreshToken(RefreshTokenRequest request);
}
