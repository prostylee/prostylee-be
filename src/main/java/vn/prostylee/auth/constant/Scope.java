package vn.prostylee.auth.constant;

import static vn.prostylee.auth.constant.AuthRoleConstants.GRANTED_AUTHORITY_PREFIX;

/**
 * The scope of token
 */
public enum Scope {

    REFRESH_TOKEN;

    public String authority() {
        return GRANTED_AUTHORITY_PREFIX + this.name();
    }
}
