package vn.prostylee.auth.constant;

import java.util.UUID;

public final class AuthConstants {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer";
    public static final String GOOGLE_PASSWORD_TOKEN = UUID.randomUUID().toString();
    public static final String OAUTH_KEY = "X-PS-Authorization-Type";
    public static final String OAUTH_VALUE = "OPEN-ID";

    private AuthConstants() {

    }
}