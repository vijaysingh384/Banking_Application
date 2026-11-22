package com.example.banking_backend.Exception;

import com.example.banking_backend.Model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> genericException(Exception e , WebRequest webRequest){
        return buildErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong", webRequest);
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
        ErrorDetails details = new ErrorDetails();
        details.setDateTime(LocalDateTime.now());
        details.setStatus(status.value());
        details.setMsg(message);
        details.setPath(webRequest.getDescription(false));
        return new ResponseEntity<>(details, status);
    }

}
