package com.finby.exception.exception_handler;

import com.finby.exception.ImagePersistenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class ImagePersistenceExceptionHandler {

    @ExceptionHandler(ImagePersistenceException.class)
    public ResponseEntity<String> handelImagePersistenceException(ImagePersistenceException imagePersistenceException) {
        return new ResponseEntity<>(imagePersistenceException.getMessage(), INTERNAL_SERVER_ERROR);
    }
}