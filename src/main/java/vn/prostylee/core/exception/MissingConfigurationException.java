package vn.prostylee.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Missing resource exception
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MissingConfigurationException extends RuntimeException {

	public MissingConfigurationException(String message) {
		super(message);
	}

	public MissingConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

}
