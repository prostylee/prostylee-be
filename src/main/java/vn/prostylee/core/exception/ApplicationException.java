package vn.prostylee.core.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class ApplicationException extends RuntimeException {

    private final String errorMessage;

    private final HttpStatus errorStatus;

    public ApplicationException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ApplicationException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorMessage = errorMessage;
        this.errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
