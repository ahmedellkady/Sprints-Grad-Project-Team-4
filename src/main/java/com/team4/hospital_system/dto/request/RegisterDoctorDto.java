package com.team4.hospital_system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.DayOfWeek;
import java.util.Set;

@Data
public class RegisterDoctorDto {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String hashedPassword;

    @NotEmpty(message = "At least one working day must be specified")
    private Set<DayOfWeek> workingDays;
}
