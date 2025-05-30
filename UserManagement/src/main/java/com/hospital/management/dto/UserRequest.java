package com.hospital.management.dto;

import com.hospital.management.entity.Role;

import lombok.Data;

@Data
public class UserRequest {
	
	// Common fields
    private String name;
    private String gender;
    private String phone;
    private String email;
    private Role role;
    private String password;
    
 // Doctor-specific fields
    private String specialization;
    private String qualification;
    private String roomNumber;

    // Patient-specific fields
    private String disease;
    private String place;
}