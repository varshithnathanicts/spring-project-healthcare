package com.hospital.management.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.hospital.management.exception.UserNotFoundException;

import org.slf4j.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle specific custom exception
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex) {
         logger.error("User not found: {}", ex.getMessage(), ex);
         ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
         return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
    
    // Generic Exception Handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllExceptions(Exception ex) {
         logger.error("An error occurred: {}", ex.getMessage(), ex);
         ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage());
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
}
