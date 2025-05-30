package com.hospital.management.controller;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.hospital.management.dto.UserUpdateDto;
import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.Patient;
import com.hospital.management.entity.User;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.service.UserService;

@RestController
@RequestMapping("/hospital/profile")
public class ProfileController {

    // Inject the service via its interface rather than the implementation.
    private final UserService userService;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public ProfileController(UserService userService,
                             DoctorRepository doctorRepository,
                             PatientRepository patientRepository) {
        this.userService = userService;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @GetMapping("/doctorsList")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(doctorRepository.findAll());
    }
    
    @GetMapping("/patientsList")
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientRepository.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable Long userId) {
        Optional<User> userOpt = userService.findById(userId);
        return userOpt.<ResponseEntity<?>>map(user -> ResponseEntity.ok(user))
                      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                                                     .body("User not found"));
    }


    @PutMapping("/{userId}")
    public ResponseEntity<?> updateProfile(@PathVariable Long userId, @RequestBody @Valid UserUpdateDto dto) {
        User updatedUser = userService.updateUserProfile(userId, dto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long userId) {
        if (userService.deleteUser(userId)) {
            return ResponseEntity.ok("Profile deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
}
