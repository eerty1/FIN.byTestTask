package com.finby.exception.exception_handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class IllegalStateExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handelIllegalStateException(IllegalStateException illegalStateException) {
        return new ResponseEntity<>(illegalStateException.getMessage(), BAD_REQUEST);
    }
}
