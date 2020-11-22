package vn.prostylee.media.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Google Drive resource exception
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class GoogleDriveException extends RuntimeException {

	public GoogleDriveException(String message) {
		super(message);
	}

	public GoogleDriveException(String message, Throwable cause) {
		super(message, cause);
	}

}