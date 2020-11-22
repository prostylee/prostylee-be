package vn.prostylee.auth.exception;

public class InvalidJwtToken extends AuthenticationException {

    public InvalidJwtToken(String errorMessage) {
        super(errorMessage);
    }

    public InvalidJwtToken(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
