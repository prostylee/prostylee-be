package vn.prostylee.auth.token.verifier;

import vn.prostylee.auth.configure.properties.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.prostylee.auth.token.parser.TokenParser;

@RequiredArgsConstructor
@Component
public class ExpirationTokenVerifier implements TokenVerifier {

    private final SecurityProperties securityProperties;

    private final TokenParser tokenParser;

    @Override
    public boolean verify(String token) {
        return tokenParser.parseClaims(token, securityProperties.getJwt().getTokenSigningKey()) != null;
    }
}
