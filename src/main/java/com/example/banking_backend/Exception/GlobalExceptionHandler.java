package com.example.banking_backend.Exception;

import com.example.banking_backend.Model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> genericException(Exception e , WebRequest webRequest){
        return buildErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong", webRequest);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> validationFailed(MethodArgumentNotValidException e, WebRequest webRequest){
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            fieldErrors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ErrorDetails details = errorDetails(HttpStatus.BAD_REQUEST, "Validation failed", webRequest);
        details.setFieldErrors(fieldErrors);
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetails> illegalArgument(IllegalArgumentException e, WebRequest webRequest){
        return buildErrorDetails(HttpStatus.BAD_REQUEST, e.getMessage(), webRequest);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class})
    public ResponseEntity<ErrorDetails> badRequest(Exception e, WebRequest webRequest){
        return buildErrorDetails(HttpStatus.BAD_REQUEST, "Invalid request: " + e.getMessage(), webRequest);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorDetails> resourceNotFound(NoResourceFoundException e, WebRequest webRequest){
        return buildErrorDetails(HttpStatus.NOT_FOUND, "Resource not found", webRequest);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetails> badCredentials(BadCredentialsException e, WebRequest webRequest){
        return buildErrorDetails(HttpStatus.UNAUTHORIZED, "Invalid username or password", webRequest);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> accessDenied(AccessDeniedException e, WebRequest webRequest){
        return buildErrorDetails(HttpStatus.FORBIDDEN, e.getMessage(), webRequest);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorDetails> conflictingUpdate(ObjectOptimisticLockingFailureException e, WebRequest webRequest){
        return buildErrorDetails(HttpStatus.CONFLICT, "The record was changed by another request, please retry", webRequest);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> userNotFound(ResourceNotFoundException e, WebRequest request){
        return buildErrorDetails(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    private ResponseEntity<ErrorDetails> buildErrorDetails(HttpStatus status, String message, WebRequest webRequest) {
        return new ResponseEntity<>(errorDetails(status, message, webRequest), status);
    }

    private ErrorDetails errorDetails(HttpStatus status, String message, WebRequest webRequest) {
        ErrorDetails details = new ErrorDetails();
        details.setDateTime(LocalDateTime.now());
        details.setStatus(status.value());
        details.setMsg(message);
        details.setPath(webRequest.getDescription(false));
        return details;
    }

}
