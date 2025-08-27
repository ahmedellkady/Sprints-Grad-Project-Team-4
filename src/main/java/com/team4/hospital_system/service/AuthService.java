package com.team4.hospital_system.service;

import com.team4.hospital_system.dto.request.*;
import com.team4.hospital_system.dto.response.AppointmentDto;
import com.team4.hospital_system.dto.response.AuthResponseDto;
import com.team4.hospital_system.dto.response.UserDto;

import java.time.LocalDateTime;
import java.util.List;

public interface AuthService {
    AuthResponseDto login(LoginDto request);
    void logout(String refreshToken);
    AuthResponseDto refresh(RefreshTokenDto request);

    // Optional simple registrations for MVP
    UserDto registerPatient(RegisterPatientDto request);
    UserDto registerDoctor(RegisterDoctorDto request);
    UserDto registerPharmacy(RegisterPharmacyDto request);

}
