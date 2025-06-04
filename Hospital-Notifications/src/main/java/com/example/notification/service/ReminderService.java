package com.example.notification.service;

import com.example.notification.model.Appointment;
import com.example.notification.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ReminderService {
    private static final Logger logger = LoggerFactory.getLogger(ReminderService.class);

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TwilioSmsService smsService;

    @Scheduled(fixedRate = 600000) // every 10 minutes
    public void sendHourlyReminders() {
        logger.info("Running hourly reminder job...");
        List<Appointment> upcomingAppointments = getAppointmentsInNextHour();

        for (Appointment app : upcomingAppointments) {
            String formattedDate = formatDate(app.getDate());
            String message = "Reminder: You have an appointment at " + formattedDate;
            logger.info("Sending reminder for appointment ID: {}", app.getId());

            emailService.sendEmail(app.getDoctor().getEmail(), "Appointment Reminder", message);
            emailService.sendEmail(app.getPatient().getEmail(), "Appointment Reminder", message);

            smsService.sendSms(app.getDoctor().getPhoneNumber(), message);
            smsService.sendSms(app.getPatient().getPhoneNumber(), message);
        }

        logger.info("Reminder job completed. Total reminders sent: {}", upcomingAppointments.size() * 2);
    }

    @Scheduled(cron = "0 0 8 * * ?") // Runs daily at 8 AM
    public void sendDailyReminders() {
        logger.info("Running daily reminder job...");
        List<Appointment> upcomingAppointments = getTomorrowAppointments();

        for (Appointment app : upcomingAppointments) {
            String formattedDate = formatDate(app.getDate());
            String message = "Reminder: You have an appointment tomorrow at " + formattedDate;
            logger.info("Sending reminder for appointment ID: {}", app.getId());

            emailService.sendEmail(app.getDoctor().getEmail(), "Appointment Reminder", message);
            emailService.sendEmail(app.getPatient().getEmail(), "Appointment Reminder", message);

            smsService.sendSms(app.getDoctor().getPhoneNumber(), message);
            smsService.sendSms(app.getPatient().getPhoneNumber(), message);
        }

        logger.info("Daily reminder job completed. Total reminders sent: {}", upcomingAppointments.size() * 2);
    }

    private List<Appointment> getAppointmentsInNextHour() {
        Calendar now = Calendar.getInstance();
        Calendar oneHourLater = Calendar.getInstance();
        oneHourLater.add(Calendar.MINUTE, 60);

        Date start = now.getTime();
        Date end = oneHourLater.getTime();

        logger.debug("Fetching appointments between {} and {}", start, end);
        return appointmentRepository.findByDateBetween(start, end);
    }

    private List<Appointment> getTomorrowAppointments() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        Date tomorrow = cal.getTime();
        logger.debug("Fetching appointments for date: {}", tomorrow);
        return appointmentRepository.findByDate(tomorrow);
    }

    private String formatDate(Date date) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.of("Asia/Kolkata"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return formatter.format(zonedDateTime);
    }
}
