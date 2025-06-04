package com.example.notification.controller;

import com.example.notification.exception.AppointmentCreationException;
import com.example.notification.exception.UserNotFoundException;
import com.example.notification.exception.InvalidAppointmentTimeException;
import com.example.notification.exception.NotificationFailureException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorTestController {

    @GetMapping("/test/appointment")
    public String testAppointmentError(@RequestParam boolean trigger) {
        if (trigger) {
            throw new AppointmentCreationException("Simulated appointment creation failure.");
        }
        return "Appointment creation test passed.";
    }

    @GetMapping("/test/user")
    public String testUserError(@RequestParam boolean trigger) {
        if (trigger) {
            throw new UserNotFoundException("Simulated user not found.");
        }
        return "User test passed.";
    }

    @GetMapping("/test/invalid-time")
    public String testInvalidTime(@RequestParam boolean trigger) {
        if (trigger) {
            throw new InvalidAppointmentTimeException("Simulated invalid appointment time.");
        }
        return "Appointment time test passed.";
    }

    @GetMapping("/test/notification")
    public String testNotificationError(@RequestParam boolean trigger) {
        if (trigger) {
            throw new NotificationFailureException("Simulated notification failure.");
        }
        return "Notification test passed.";
    }
}
