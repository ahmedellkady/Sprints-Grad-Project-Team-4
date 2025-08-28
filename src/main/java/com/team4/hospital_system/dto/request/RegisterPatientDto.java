package com.team4.hospital_system.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterPatientDto {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotNull(message = "Age cannot be null")
    @Min(value = 0, message = "Age cannot be negative")
    private int age;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String hashedPassword;
}
