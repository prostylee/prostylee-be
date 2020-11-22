package vn.prostylee.auth.token.extractor;

import vn.prostylee.auth.constant.Auth;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * An implementation of {@link TokenExtractor} extracts token from Authorization: Bearer scheme.
 */
@Component
public class JwtHeaderTokenExtractor implements TokenExtractor {

    @Override
    public String extract(HttpServletRequest request) {
        String bearerToken = request.getHeader(Auth.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(Auth.BEARER_PREFIX)) {
            return bearerToken.substring(Auth.BEARER_PREFIX.length());
        }
        return bearerToken;
    }
}
