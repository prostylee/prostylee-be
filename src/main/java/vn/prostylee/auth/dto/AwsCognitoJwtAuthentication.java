package vn.prostylee.auth.dto;

import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import vn.prostylee.auth.dto.response.UserCredential;

import java.util.Collection;

public class AwsCognitoJwtAuthentication extends AbstractAuthenticationToken {

    private final transient UserCredential principal;

    private final JWTClaimsSet jwtClaimsSet;

    public AwsCognitoJwtAuthentication(UserCredential principal, JWTClaimsSet jwtClaimsSet, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.jwtClaimsSet = jwtClaimsSet;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public JWTClaimsSet getJwtClaimsSet() {
        return jwtClaimsSet;
    }


}