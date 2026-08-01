package com.example.banking_backend.Exception;

import com.example.banking_backend.Model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> genericException(Exception e , WebRequest webRequest){
        ErrorDetails errordetails = new ErrorDetails();
        errordetails.setDateTime(LocalDateTime.now());
        errordetails.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errordetails.setMsg("Something went wrong");
        errordetails.setPath(webRequest.getDescription(false));
        return new ResponseEntity<>(errordetails,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> userNotFound(ResourceNotFoundException e, WebRequest request){
        ErrorDetails details = new ErrorDetails();
        details.setDateTime(LocalDateTime.now());
        details.setStatus(HttpStatus.NOT_FOUND.value());
        details.setMsg(e.getMessage());
        details.setPath(request.getDescription(false));
        return  new ResponseEntity<>(details,HttpStatus.NOT_FOUND);
    }

}
