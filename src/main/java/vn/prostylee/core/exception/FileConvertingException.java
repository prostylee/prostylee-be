package vn.prostylee.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FileConvertingException extends RuntimeException {

    public FileConvertingException(String message) {
        super(message);
    }

    public FileConvertingException(String message, Throwable cause) {
        super(message, cause);
    }
}
