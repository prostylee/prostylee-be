package vn.prostylee.auth.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static vn.prostylee.auth.constant.AuthRoleConstants.GRANTED_AUTHORITY_PREFIX;

@AllArgsConstructor
public enum AuthRole {

    SUPER_ADMIN(AuthRoleConstants.SUPER_ADMIN),
    STORE_OWNER(AuthRoleConstants.STORE_OWNER),
    BUYER(AuthRoleConstants.BUYER);

    @Getter
    private final String roleName;

    public SimpleGrantedAuthority grantedAuthority() {
        return buildGrantedAuthority(roleName);
    }

    public static SimpleGrantedAuthority buildGrantedAuthority(String roleName) {
        return new SimpleGrantedAuthority(GRANTED_AUTHORITY_PREFIX + roleName);
    }
}
