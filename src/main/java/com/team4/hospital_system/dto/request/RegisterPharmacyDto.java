package com.team4.hospital_system.dto.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterPharmacyDto {
    @NotBlank(message = "Pharmacy name is required")
    private String name;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String hashedPassword;

    @Pattern(regexp = "^\\+20\\d{10}$", message = "Phone number must start with +20 and be followed by 10 digits")
    private String phoneNumber;
}
