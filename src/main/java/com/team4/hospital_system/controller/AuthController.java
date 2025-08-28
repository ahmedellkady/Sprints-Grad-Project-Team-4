package com.team4.hospital_system.controller;

import com.team4.hospital_system.dto.request.LoginDto;
import com.team4.hospital_system.dto.request.RegisterDoctorDto;
import com.team4.hospital_system.dto.request.RegisterPatientDto;
import com.team4.hospital_system.dto.request.RegisterPharmacyDto;
import com.team4.hospital_system.dto.response.AuthResponseDto;
import com.team4.hospital_system.dto.response.UserDto;
import com.team4.hospital_system.service.AuthService;
import com.team4.hospital_system.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register/patient")
    public ResponseEntity<RegisterPatientDto> registerPatient(@Valid @RequestBody RegisterPatientDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerPatient(request));
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<RegisterDoctorDto> registerDoctor(@Valid @RequestBody RegisterDoctorDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerDoctor(request));
    }

    @PostMapping("/register/pharmacy")
    public ResponseEntity<RegisterPharmacyDto> registerPharmacy(@Valid @RequestBody RegisterPharmacyDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerPharmacy(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginDto request) {
        return ResponseEntity.ok(authService.login(request));
    }
//
//    @PostMapping("/refresh")
//    public ResponseEntity<AuthResponseDto> refresh(@Valid @RequestBody RefreshTokenDto request) {
//        return ResponseEntity.ok(authService.refresh(request));
//    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenDto request) {
//        authService.logout(request.getRefreshToken());
//        return ResponseEntity.ok().build();
//    }
}
