package com.finby.exception.exception_handler;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class EmptyQueryResultExceptionsHandler {

    @ExceptionHandler({EntityNotFoundException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<String> handelEmptyQueryResultExceptions() {
        return new ResponseEntity<>("Requested entity wasn't found", NOT_FOUND);
    }
}
