package vn.prostylee.auth.token.extractor;

import javax.servlet.http.HttpServletRequest;

/**
 * The implementation of this interface should always return raw base-64 encoded representation of JWT Token.
 */
public interface TokenExtractor {

    /**
     * Extract the token from user request
     *
     * @param request The http request
     * @return the jwt token
     */
    String extract(HttpServletRequest request);
}
