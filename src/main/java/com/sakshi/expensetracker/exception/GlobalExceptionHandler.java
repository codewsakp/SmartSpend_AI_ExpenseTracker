
package com.sakshi.expensetracker.exception;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle expense not found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(
            EntityNotFoundException exception) {

        Map<String, String> error = new HashMap<>();
        error.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity
                .badRequest()
                .body(errors);
    }
}
