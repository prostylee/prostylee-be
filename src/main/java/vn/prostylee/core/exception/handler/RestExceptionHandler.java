package vn.prostylee.core.exception.handler;

import vn.prostylee.auth.exception.AuthenticationException;
import vn.prostylee.core.dto.response.ApiErrorResponse;
import vn.prostylee.core.dto.response.ValidationError;
import vn.prostylee.core.validator.UniqueEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.security.GeneralSecurityException;
import java.util.*;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    // 400 : Argument Not Valid
    @Override
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                               HttpStatus status, WebRequest request) {
        log.error("handleMethodArgumentNotValid: ", ex);
        //
        ApiErrorResponse response = new ApiErrorResponse();
        response.setCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage("Validation Failed");
        response.setTrace(ex.getMessage());
        response.setPath(request.getContextPath());

        // Create ValidationError instances
        addFieldErrorsToResponse(response, ex.getBindingResult().getFieldErrors());
        addGlobalErrorsToResponse(response, ex.getBindingResult().getGlobalErrors());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private void addFieldErrorsToResponse(ApiErrorResponse response, List<FieldError> fieldErrors) {
        for (FieldError fe : fieldErrors) {
            List<ValidationError> validationErrors = response.getErrors().computeIfAbsent(fe.getField(), k -> new ArrayList<>());
            ValidationError validationError = new ValidationError();
            validationError.setCode(fe.getCode());
            validationError.setMessage(messageSource.getMessage(fe, null));
            validationErrors.add(validationError);
        }
    }

    private void addGlobalErrorsToResponse(ApiErrorResponse response, List<ObjectError> globalErrors) {
        for (final ObjectError error : globalErrors) {
            final String[] keyGlobalErrors = new String[1];

            Optional<Annotation> annotation = getSpringValidatorAdapter(error)
                    .map(ConstraintViolation::getConstraintDescriptor)
                    .map(ConstraintDescriptor::getAnnotation);
            if (annotation.isPresent()) {
                if (annotation.get() instanceof UniqueEntity) {
                    UniqueEntity uniqueEntityAnnotation = (UniqueEntity) annotation.get();
                    if (uniqueEntityAnnotation.fieldNames().length > 0) {
                        keyGlobalErrors[0] = uniqueEntityAnnotation.fieldNames()[0];
                    }
                }
            } else {
                keyGlobalErrors[0] = error.getObjectName();
            }

            List<ValidationError> validationErrors = response.getErrors()
                    .computeIfAbsent(keyGlobalErrors[0], k -> new ArrayList<>());

            ValidationError validationError = new ValidationError();
            validationError.setCode(error.getCode());
            validationError.setMessage(messageSource.getMessage(error, Locale.getDefault()));
            validationErrors.add(validationError);
        }
    }

    private Optional<ConstraintViolation> getSpringValidatorAdapter(ObjectError error) {
        try {
            return Optional.ofNullable((ConstraintViolation) FieldUtils.readField(error, "violation", true));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // 400 : Bind Exception
    @Override
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers,
                                                         final HttpStatus status, final WebRequest request) {
        log.error("handleBindException: ", ex);
        //
        ApiErrorResponse response = new ApiErrorResponse();
        response.setCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage("Binding Failed");
        response.setTrace(ex.getMessage());
        response.setPath(request.getContextPath());

        // Create ValidationError instances
        addFieldErrorsToResponse(response, ex.getBindingResult().getFieldErrors());
        addGlobalErrorsToResponse(response, ex.getBindingResult().getGlobalErrors());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 400 : Type Mismatch
    @Override
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers,
                                                        final HttpStatus status, final WebRequest request) {
        log.error("handleTypeMismatch: ", ex);
        //
        final String error = "Type Mismatch: " + ex.getValue() +
                " value for " + ex.getPropertyName() +
                " should be of type " + ex.getRequiredType();

        ApiErrorResponse response = new ApiErrorResponse();
        response.setCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage(error);
        response.setTrace(ex.getMessage());
        response.setPath(request.getContextPath());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 400 : Missing Servlet Request Part
    @Override
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex,
                                                                     final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.error("handleMissingServletRequestPart: ", ex);
        //
        final String error = "Missing Servlet Request Part: " + ex.getRequestPartName() + " part is missing";

        ApiErrorResponse response = new ApiErrorResponse();
        response.setCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage(error);
        response.setTrace(ex.getMessage());
        response.setPath(request.getContextPath());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 400 : Missing Servlet Request Parameter
    @Override
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status,
            final WebRequest request) {
        log.error("handleMissingServletRequestParameter: ", ex);
        //
        final String error = "Missing Servlet Request Parameter: " + ex.getParameterName() + " parameter is missing";

        ApiErrorResponse response = new ApiErrorResponse();
        response.setCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage(error);
        response.setTrace(ex.getMessage());
        response.setPath(request.getContextPath());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 400 : Argument Type Mismatch
    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        log.error("handleMethodArgumentTypeMismatch: ", ex);
        //
        final String error = "Method Argument Type Mismatch: " + ex.getName() +
                " should be of type " + ex.getRequiredType().getName();

        ApiErrorResponse response = new ApiErrorResponse();
        response.setCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage(error);
        response.setTrace(ex.getMessage());
        response.setPath(request.getRequestURL().toString());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 400 : Constraint Violation
    @ExceptionHandler({ ConstraintViolationException.class })
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex,
                                                            final HttpServletRequest request) {
        log.error("handleConstraintViolation: ", ex);
        //
        ApiErrorResponse response = new ApiErrorResponse();
        response.setCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage("Constraint Violation");
        response.setTrace(ex.getMessage());
        response.setPath(request.getRequestURL().toString());

        // Create ValidationError instances
        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            List<ValidationError> validationErrors = response.getErrors().computeIfAbsent(String.valueOf(violation.getPropertyPath()), k -> new ArrayList<>());
            ValidationError validationError = new ValidationError();
            validationError.setCode(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath());
            validationError.setMessage(violation.getMessage());
            validationErrors.add(validationError);
        }

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 404
    @Override
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex,
                                                                   final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.error("handleNoHandlerFoundException: ", ex);
        //
        final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

        ApiErrorResponse response = new ApiErrorResponse();
        response.setCode(HttpStatus.NOT_FOUND.value());
        response.setMessage(error);
        response.setTrace(ex.getMessage());
        response.setPath(request.getContextPath());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // 405
    @Override
    @ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status,
            final WebRequest request) {
        log.error("handleHttpRequestMethodNotSupported: ", ex);
        //
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        Optional.ofNullable(ex.getSupportedHttpMethods()).orElse(new HashSet<>()).forEach(t -> builder.append(t).append(" "));

        ApiErrorResponse response = new ApiErrorResponse();
        response.setCode(HttpStatus.METHOD_NOT_ALLOWED.value());
        response.setMessage("Http Request Method Not Supported");
        response.setTrace(builder.toString() + ". " + ex.getMessage());
        response.setPath(request.getContextPath());

        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    // 415
    @Override
    @ResponseStatus(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex,
                                                                     final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.error("handleHttpMediaTypeNotSupported: ", ex);
        //
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(" "));

        ApiErrorResponse response = new ApiErrorResponse();
        response.setCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        response.setMessage("Http Media Type Not Supported");
        response.setTrace(builder.toString() + ". " + ex.getMessage());
        response.setPath(request.getContextPath());

        return new ResponseEntity<>(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    // 500
    @Override
    @ResponseStatus(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    protected ResponseEntity<Object> handleHttpMessageNotWritable(final HttpMessageNotWritableException ex,
                                                                  final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.error("handleHttpMessageNotWritableException: ", ex);
        //

        ApiErrorResponse response = new ApiErrorResponse();
        response.setCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        response.setMessage("Http Message Not Writable");
        response.setTrace(ex.getMessage());
        response.setPath(request.getContextPath());

        return new ResponseEntity<>(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    // General Security Exception
    @ExceptionHandler(GeneralSecurityException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleGeneralSecurityException(GeneralSecurityException ex, HttpServletRequest request) {
        log.error("GeneralSecurityException: ", ex);
        //
        ApiErrorResponse response = new ApiErrorResponse();
        response.setCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage("Key pair is not valid");
        response.setTrace(ex.getMessage());
        response.setPath(request.getRequestURL().toString());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConflict(RuntimeException ex, HttpServletRequest request) {
        log.debug("handleConflict: ", ex);
        //
        ApiErrorResponse response = new ApiErrorResponse();
        response.setCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage("Illegal argument exception");
        response.setTrace(ex.getMessage());
        response.setPath(request.getRequestURL().toString());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Bad credentials
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest request) {
        log.error("handleBadCredentialsException: ", ex);
        //

        ApiErrorResponse response = new ApiErrorResponse();
        response.setCode(HttpStatus.UNAUTHORIZED.value());
        response.setMessage("Bad credentials");
        response.setTrace(ex.getMessage());
        response.setPath(request.getRequestURL().toString());

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // 400: Unauthorized
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        log.error("handleAuthenticationException: ", ex);
        //
        ApiErrorResponse response = new ApiErrorResponse();
        response.setCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage(ex.getMessage());
        response.setTrace(Arrays.asList(ex.getStackTrace()).toString());
        response.setPath(request.getRequestURL().toString());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 500 : All Handler
    @ExceptionHandler({ Exception.class })
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAll(final Exception ex, HttpServletRequest request) {
        log.error("handleAll: ", ex);
        //
        ApiErrorResponse response = new ApiErrorResponse();
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage("Internal server error");
        response.setTrace(ex.getMessage());
        response.setPath(request.getRequestURL().toString());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 500 : Http Message Not Readable
    @Override
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("handleHttpMessageNotReadable: ", ex);
        //
        ApiErrorResponse response = new ApiErrorResponse();
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage("Http Message Not Readable");
        response.setTrace(ex.getMessage());
        response.setPath(request.getContextPath());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}