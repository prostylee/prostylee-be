package vn.prostylee.auth.constant;

public enum AuthRole {
    SUPER_ADMIN, STORE_OWNER, BUYER;

    public String authority() {
        return "ROLE_" + this.name();
    }
}
