package vn.prostylee.auth.configure.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.prostylee.auth.configure.properties.SecurityProperties;
import vn.prostylee.auth.constant.AuthConstants;
import vn.prostylee.auth.constant.AuthRole;
import vn.prostylee.auth.dto.response.UserCredential;
import vn.prostylee.auth.token.extractor.TokenExtractor;
import vn.prostylee.auth.token.parser.TokenParser;
import vn.prostylee.auth.token.verifier.TokenVerifier;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends AuthOncePerRequestFilter {

    private final SecurityProperties securityProperties;

    private final TokenExtractor tokenExtractor;

    private final TokenVerifier tokenVerifier;

    private final TokenParser tokenParser;

    @Override
    boolean setAuthIfTokenValid(HttpServletRequest request) {
        log.debug("Checking system Jwt");
        String jwt = tokenExtractor.extract(request);

        if (StringUtils.isBlank(request.getHeader(AuthConstants.OAUTH_KEY)) && StringUtils.isNotBlank(jwt) && tokenVerifier.verify(jwt)) {
            UserCredential user = tokenParser.getUserCredential(jwt, securityProperties.getJwt().getTokenSigningKey());

            // UsernamePasswordAuthenticationToken: A built-in object, used by spring to represent the current authenticated / being authenticated user.
            // It needs a list of authorities, which has type of GrantedAuthority interface, where SimpleGrantedAuthority is an implementation of that interface
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    user, null, user.getRoles().stream()
                    .map(AuthRole::buildGrantedAuthority)
                    .collect(Collectors.toList()));

            SecurityContextHolder.getContext().setAuthentication(auth);
            return true;
        }
        return false;
    }
}
