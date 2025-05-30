package com.hospital.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PatientProfileNotFoundException extends RuntimeException {
    public PatientProfileNotFoundException(String message) {
        super(message);
    }
}
