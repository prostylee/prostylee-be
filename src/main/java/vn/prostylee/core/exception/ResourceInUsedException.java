package vn.prostylee.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class ResourceInUsedException extends RuntimeException {

    public ResourceInUsedException(String message) {
        super(message);
    }

    public ResourceInUsedException(String message, Throwable cause) {
        super(message, cause);
    }
}
