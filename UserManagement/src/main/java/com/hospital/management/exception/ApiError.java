package com.hospital.management.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

public class ApiError {
    
    private HttpStatus status;
    private String message;
    private LocalDateTime timestamp;

    // Default constructor that sets the timestamp to now.
    public ApiError() {
        this.timestamp = LocalDateTime.now();
    }

    // Convenient constructor.
    public ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and setters.
    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
