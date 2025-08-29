package com.team4.hospital_system.dto.request;

import lombok.Data;

@Data
public class LoginDto {
    private final String email;
    private final String hashedPassword;
}
