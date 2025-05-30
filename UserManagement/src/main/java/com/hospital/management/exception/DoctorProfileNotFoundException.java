package com.hospital.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DoctorProfileNotFoundException extends RuntimeException {
    public DoctorProfileNotFoundException(String message) {
        super(message);
    }
}
