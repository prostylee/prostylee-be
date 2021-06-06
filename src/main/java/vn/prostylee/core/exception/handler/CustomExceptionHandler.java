package vn.prostylee.core.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.prostylee.core.dto.response.ApiErrorResponse;
import vn.prostylee.core.exception.ApplicationException;
import vn.prostylee.core.exception.ResourceInUsedException;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.exception.ValidatingException;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    // Resource Not Found Exception
    @ExceptionHandler({ ResourceNotFoundException.class})
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        log.error("ResourceNotFoundException: ", ex);
        //
        ApiErrorResponse response = new ApiErrorResponse();
        response.setCode(HttpStatus.NOT_FOUND.value());
        response.setMessage(ex.getMessage());
        response.setTrace(buildTrace(ex));
        response.setPath(request.getRequestURL().toString());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Resource Resource In Used Exception
    @ExceptionHandler({ ResourceInUsedException.class})
    @ResponseStatus(code = HttpStatus.FAILED_DEPENDENCY)
    public ResponseEntity<ApiErrorResponse> handleResourceInUsedException(ResourceInUsedException ex, HttpServletRequest request) {
        log.error("ResourceInUsedException: ", ex);
        //
        ApiErrorResponse response = new ApiErrorResponse();
        response.setCode(HttpStatus.FAILED_DEPENDENCY.value());
        response.setMessage(ex.getMessage());
        response.setTrace(buildTrace(ex));
        response.setPath(request.getRequestURL().toString());

        return new ResponseEntity<>(response, HttpStatus.FAILED_DEPENDENCY);
    }

    // Resource Not Found Exception
    @ExceptionHandler({ ValidatingException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleValidatingException(ResourceNotFoundException ex, HttpServletRequest request) {
        log.error("ValidatingException: ", ex);
        //
        ApiErrorResponse response = new ApiErrorResponse();
        response.setCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage(ex.getMessage());
        response.setTrace(buildTrace(ex));
        response.setPath(request.getRequestURL().toString());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Application exception
    @ExceptionHandler(ApplicationException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiErrorResponse> handleBadCredentialsException(ApplicationException ex, HttpServletRequest request) {
        log.error("ApplicationException: ", ex);
        //
        ApiErrorResponse response = new ApiErrorResponse();
        response.setCode(ex.getErrorStatus().value());
        response.setMessage(ex.getMessage());
        response.setTrace(buildTrace(ex));
        response.setPath(request.getRequestURL().toString());

        return new ResponseEntity<>(response, ex.getErrorStatus());
    }

    private String buildTrace(Throwable e) {
        if (e == null) {
            return "";
        }

        final int max = 20;
        int currentCause = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(e.getMessage());

        sb.append("\nCause: \n");
        while (e.getCause() != null && currentCause < max) {
            sb.append("\n ").append(e.getCause());
            e = e.getCause();
            currentCause++;
        }

        return sb.toString();
    }
}