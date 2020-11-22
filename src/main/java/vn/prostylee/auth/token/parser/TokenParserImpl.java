package vn.prostylee.auth.token.parser;

import vn.prostylee.auth.dto.response.UserCredential;
import vn.prostylee.auth.exception.InvalidJwtToken;
import vn.prostylee.auth.exception.JwtExpiredTokenException;
import vn.prostylee.core.utils.JsonUtils;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TokenParserImpl implements TokenParser {

    /**
     * Parses and validates JWT Token signature.
     *
     * @throws InvalidJwtToken if the token is invalid
     * @throws JwtExpiredTokenException if the token is expired
     *
     * @return Jws<Claims> The Claims of token
     */
    @Override
    public Jws<Claims> parseClaims(String token, String signingKey) {
        try {
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            log.error("Invalid JWT Token", ex);
            throw new InvalidJwtToken("Invalid JWT token");
        } catch (ExpiredJwtException expiredEx) {
            log.info("JWT Token is expired", expiredEx);
            throw new JwtExpiredTokenException("JWT Token is expired");
        }
    }

    @Override
    public UserCredential getUserCredential(String token, String signingKey) {
        Jws<Claims> claimsJws = this.parseClaims(token, signingKey);
        return getUserCredential(claimsJws);
    }

    @Override
    public UserCredential getUserCredential(Jws<Claims> claimsJws) {
        return JsonUtils.fromJson(claimsJws.getBody().getSubject(), UserCredential.class);
    }

    @Override
    public Long getUserIdFromJWT(String token, String signingKey) {
        return getUserCredential(token, signingKey).getId();
    }
}
