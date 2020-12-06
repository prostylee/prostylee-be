package vn.prostylee.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.prostylee.auth.constant.AuthConstants;
import vn.prostylee.auth.dto.AuthUserDetails;
import vn.prostylee.auth.dto.response.JwtAuthenticationToken;
import vn.prostylee.auth.token.AccessToken;
import vn.prostylee.auth.token.factory.JwtTokenFactory;

public class AuthenticationServiceCommon {
    @Autowired
    private JwtTokenFactory tokenFactory;

      protected JwtAuthenticationToken createResponse(AuthUserDetails userDetail) {
        AccessToken accessToken = tokenFactory.createAccessToken(userDetail);
        JwtAuthenticationToken token = JwtAuthenticationToken.builder()
                .accessToken(accessToken.getToken())
                .refreshToken(tokenFactory.createRefreshToken(userDetail).getToken())
                .tokenType(AuthConstants.BEARER_PREFIX)
                .build();
        return token;
    }

}
