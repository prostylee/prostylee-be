package vn.prostylee.media.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FileUploaderException extends RuntimeException {

    public FileUploaderException(String message) {
        super(message);
    }

    public FileUploaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
