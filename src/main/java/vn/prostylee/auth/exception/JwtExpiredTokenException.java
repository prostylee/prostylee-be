package vn.prostylee.auth.exception;

import vn.prostylee.auth.token.JwtToken;
import lombok.Getter;

public class JwtExpiredTokenException extends AuthenticationException {

    @Getter
    private JwtToken token;

    public JwtExpiredTokenException(String errorMessage) {
        super(errorMessage);
    }

    public JwtExpiredTokenException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    public JwtExpiredTokenException(JwtToken token, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.token = token;
    }
}
