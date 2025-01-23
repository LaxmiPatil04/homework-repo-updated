package com.rewards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import lombok.Data;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @Data
    static class ErrorResponse {
        private final String message;
        private final String timestamp;
    }
    
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        return createErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(InvalidMonthsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidMonthsException(InvalidMonthsException ex) {
        return createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(NullInputException.class)
    public ResponseEntity<ErrorResponse> handleNullInputException(NullInputException ex) {
        return createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        return createErrorResponse("Validation failed: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    private ResponseEntity<ErrorResponse> createErrorResponse(String message, HttpStatus status) {
        ErrorResponse error = new ErrorResponse(message, java.time.LocalDateTime.now().toString());
        return new ResponseEntity<>(error, status);
    }
}
