package vn.prostylee.auth.token.factory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.prostylee.auth.configure.properties.SecurityProperties;
import vn.prostylee.auth.constant.Scope;
import vn.prostylee.auth.converter.UserCredentialConverter;
import vn.prostylee.auth.dto.AuthUserDetails;
import vn.prostylee.auth.dto.response.UserCredential;
import vn.prostylee.auth.token.AccessToken;
import vn.prostylee.auth.token.RefreshToken;
import vn.prostylee.core.utils.JsonUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Factory class that should be always used to create {@link vn.prostylee.auth.token.JwtToken}.
 */
@RequiredArgsConstructor
@Component
public class JwtTokenFactory {

    public static final String TOKEN_SCOPES_KEY = "scopes";

    private final SecurityProperties securityProperties;

    /**
     * Factory method for issuing new access JWT Tokens.
     */
    public AccessToken createAccessToken(AuthUserDetails userDetail) {
        if (userDetail == null) {
            throw new IllegalArgumentException("Cannot create JWT Token without auth user details");
        }

        UserCredential subject = new UserCredentialConverter().convert(userDetail);
        List<String> scopes = userDetail.getAuthorities().stream().map(Object::toString).collect(Collectors.toList());

        Claims claims = Jwts.claims().setSubject(JsonUtils.toJson(subject));
        claims.put(TOKEN_SCOPES_KEY, scopes);

        String token = createToken(claims, securityProperties.getJwt().getAccessTokenExpirationInMs());
        return new AccessToken(token, subject);
    }

    /**
     * Factory method for issuing new refresh JWT Tokens.
     */
    public RefreshToken createRefreshToken(AuthUserDetails userDetail) {
        if (userDetail == null) {
            throw new IllegalArgumentException("Cannot create JWT Token without auth user details");
        }

        UserCredential subject = UserCredential.builder().id(userDetail.getId()).build();

        List<String> scopes = Collections.singletonList(Scope.REFRESH_TOKEN.authority());

        Claims claims = Jwts.claims().setSubject(JsonUtils.toJson(subject));
        claims.put(TOKEN_SCOPES_KEY, scopes);

        String token = createToken(claims, securityProperties.getJwt().getRefreshTokenExpirationInMs());
        return new RefreshToken(token);
    }

    private String createToken(Claims claims, int expirationInMs) {
        LocalDateTime currentTime = LocalDateTime.now();
        Date issueAt = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());
        Date expirationAt = Date.from(currentTime
                .plusMinutes(expirationInMs)
                .atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setClaims(claims)
                .setIssuer(securityProperties.getJwt().getTokenIssuer())
                .setIssuedAt(issueAt)
                .setExpiration(expirationAt)
                .signWith(SignatureAlgorithm.HS512, securityProperties.getJwt().getTokenSigningKey())
                .compact();
    }
}
