package vn.prostylee.auth.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.prostylee.auth.configure.properties.SecurityProperties;
import vn.prostylee.auth.constant.AuthConstants;
import vn.prostylee.auth.constant.Scope;
import vn.prostylee.auth.dto.AuthUserDetails;
import vn.prostylee.auth.dto.request.RefreshTokenRequest;
import vn.prostylee.auth.dto.response.JwtAuthenticationToken;
import vn.prostylee.auth.entity.Feature;
import vn.prostylee.auth.entity.User;
import vn.prostylee.auth.exception.InvalidJwtToken;
import vn.prostylee.auth.repository.UserRepository;
import vn.prostylee.auth.service.UserAuthTokenService;
import vn.prostylee.auth.token.AccessToken;
import vn.prostylee.auth.token.factory.JwtTokenFactory;
import vn.prostylee.auth.token.parser.TokenParser;
import vn.prostylee.auth.token.verifier.TokenVerifier;
import vn.prostylee.core.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAuthTokenServiceImpl implements UserAuthTokenService {

    private final JwtTokenFactory tokenFactory;

    private final TokenVerifier tokenVerifier;

    private final TokenParser tokenParser;

    private final SecurityProperties securityProperties;

    private final UserRepository userRepository;

    @Override
    public JwtAuthenticationToken createToken(AuthUserDetails userDetail) {
        AccessToken accessToken = tokenFactory.createAccessToken(userDetail);
        return JwtAuthenticationToken.builder()
                .accessToken(accessToken.getToken())
                .refreshToken(tokenFactory.createRefreshToken(userDetail).getToken())
                .tokenType(AuthConstants.BEARER_PREFIX)
                .build();
    }

    @Override
    public JwtAuthenticationToken refreshToken(RefreshTokenRequest request) {
        if (tokenVerifier.verify(request.getRefreshToken()) && checkRefreshTokenScope(request.getRefreshToken())) {
            Long userId = tokenParser.getUserIdFromJWT(request.getRefreshToken(), securityProperties.getJwt().getTokenSigningKey());
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User is not exists by getting with id " + userId));
            AuthUserDetails userDetail = new AuthUserDetails(user, this.getFeatures(user));
            AccessToken accessToken = tokenFactory.createAccessToken(userDetail);

            return JwtAuthenticationToken.builder()
                    .accessToken(accessToken.getToken())
                    .refreshToken(request.getRefreshToken())
                    .tokenType(AuthConstants.BEARER_PREFIX)
                    .build();
        }
        throw new InvalidJwtToken("The refresh token is invalid");
    }

    protected boolean checkRefreshTokenScope(String refreshToken) {
        Jws<Claims> claimsJws = tokenParser.parseClaims(refreshToken, securityProperties.getJwt().getTokenSigningKey());
        Claims claims = claimsJws.getBody();
        List<String> scopes = (List<String>) claims.get(JwtTokenFactory.TOKEN_SCOPES_KEY);
        if (CollectionUtils.isEmpty(scopes)) {
            throw new InvalidJwtToken("The refresh token is invalid scope");
        }
        for (String scope : scopes) {
            if(Scope.REFRESH_TOKEN.authority().equals(scope)) {
                return true;
            }
        }
        return false;
    }

    protected List<Feature> getFeatures(User user) {
        List<Feature> features = new ArrayList<>();
        user.getRoles().forEach(role -> features.addAll(role.getFeatures()));
        return features;
    }
}
