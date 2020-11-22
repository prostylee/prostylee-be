package vn.prostylee.auth.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class AuthenticationException extends RuntimeException {

    private final String errorMessage;

    private final HttpStatus errorStatus;

    public AuthenticationException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorStatus = HttpStatus.UNAUTHORIZED;
    }

    public AuthenticationException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorMessage = errorMessage;
        this.errorStatus = HttpStatus.UNAUTHORIZED;
    }
}