package com.example.developmentbooks.controller;

import com.example.developmentbooks.exception.NotFoundException;
import com.example.developmentbooks.exception.NotNullException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NotNullException.class)
    public ResponseEntity<?> handleNotNullException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
