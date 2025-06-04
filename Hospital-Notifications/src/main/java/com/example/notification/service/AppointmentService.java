
package com.example.notification.service;

import com.example.notification.exception.AppointmentCreationException;
import com.example.notification.model.Appointment;
import com.example.notification.model.User;
import com.example.notification.repository.AppointmentRepository;
import com.example.notification.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class AppointmentService {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TwilioSmsService smsService;

    public Appointment createAppointment(Long doctorId, Long patientId, Date date) {
        try {
            User doctor = userRepository.findById(doctorId).orElseThrow(() -> new AppointmentCreationException("Doctor not found with ID: " + doctorId));
            User patient = userRepository.findById(patientId).orElseThrow(() -> new AppointmentCreationException("Patient not found with ID: " + patientId));

            Appointment appointment = new Appointment();
            appointment.setDoctor(doctor);
            appointment.setPatient(patient);
            appointment.setDate(date);
            appointment.setStatus("CREATED");

            Appointment saved = appointmentRepository.save(appointment);
            notifyUsers(saved, "Appointment Created");
            return saved;
        } catch (Exception e) {
            logger.error("Error creating appointment: {}", e.getMessage());
            throw new AppointmentCreationException("Error creating appointment: " + e.getMessage());
        }
    }

    public Appointment modifyAppointment(Long id, Date updatedDate) {
        try {
            return appointmentRepository.findById(id).map(app -> {
                app.setDate(updatedDate);
                app.setStatus("MODIFIED");
                Appointment saved = appointmentRepository.save(app);
                notifyUsers(saved, "Appointment Modified");
                return saved;
            }).orElseThrow(() -> new AppointmentCreationException("Appointment not found with ID: " + id));
        } catch (Exception e) {
            logger.error("Error modifying appointment: {}", e.getMessage());
            throw new AppointmentCreationException("Error modifying appointment: " + e.getMessage());
        }
    }

    public void cancelAppointment(Long id) {
        try {
            appointmentRepository.findById(id).ifPresent(app -> {
                app.setStatus("CANCELLED");
                appointmentRepository.save(app);
                notifyUsers(app, "Appointment Cancelled");
            });
        } catch (Exception e) {
            logger.error("Error canceling appointment: {}", e.getMessage());
            throw new AppointmentCreationException("Error canceling appointment: " + e.getMessage());
        }
    }

    private void notifyUsers(Appointment app, String subject) {
        try {
            String formattedDate = formatDate(app.getDate());
            String message = "Your appointment on " + formattedDate + " has been " + app.getStatus();

            emailService.sendEmail(app.getDoctor().getEmail(), subject, message);
            emailService.sendEmail(app.getPatient().getEmail(), subject, message);

            smsService.sendSms(app.getDoctor().getPhoneNumber(), message);
            smsService.sendSms(app.getPatient().getPhoneNumber(), message);
        } catch (Exception e) {
            logger.error("Error sending notifications: {}", e.getMessage());
        }
    }

    private String formatDate(Date date) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.of("Asia/Kolkata"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return formatter.format(zonedDateTime);
    }
}
