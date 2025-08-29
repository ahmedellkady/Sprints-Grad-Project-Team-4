package com.team4.hospital_system.service.Impl;

import com.team4.hospital_system.dto.request.*;
import com.team4.hospital_system.dto.response.AuthResponseDto;
import com.team4.hospital_system.exception.DuplicateResourceException;
import com.team4.hospital_system.mapper.mapper;
import com.team4.hospital_system.model.*;
import com.team4.hospital_system.model.enums.Role;
import com.team4.hospital_system.repository.DoctorRepository;
import com.team4.hospital_system.repository.PatientRepository;
import com.team4.hospital_system.repository.PharmacyRepository;
import com.team4.hospital_system.repository.UserRepository;
import com.team4.hospital_system.service.AuthService;
import com.team4.hospital_system.service.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PharmacyRepository pharmacyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final mapper modelMapper;

    @Override
    public AuthResponseDto login(LoginDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getHashedPassword()
                )
        );

        // 2. If authentication is successful, fetch the user
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        // 3. Generate access and refresh tokens
        var jwt = jwtService.generateToken(request.getEmail());
//        var refreshToken = jwtService.generateRefreshToken(user);

        // 4. Return the response DTO
        return AuthResponseDto.builder()
                .token(jwt)
                .build();
    }

    @Override
    @Transactional
    public RegisterPatientDto registerPatient(RegisterPatientDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already in use.");
        }

        User user = modelMapper.toUserEntity(request);
        user.setRole(Role.PATIENT);
        user.setHashedPassword(passwordEncoder.encode(user.getHashedPassword()));
        User savedUser = userRepository.save(user);

        Patient patient = modelMapper.toPatientEntity(request);
        patient.setUser(savedUser);
        Patient savedPatient = patientRepository.save(patient);

        RegisterPatientDto responseDto = modelMapper.toRegisterPatientDto(savedUser);
        responseDto.setAge(savedPatient.getAge());
        return responseDto;
    }

    @Override
    public RegisterDoctorDto registerDoctor(RegisterDoctorDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already in use.");
        }

        User user = modelMapper.toUserEntity(request);
        user.setRole(Role.DOCTOR);
        user.setHashedPassword(passwordEncoder.encode(user.getHashedPassword()));
        User savedUser = userRepository.save(user);

        Doctor doctor = modelMapper.toDoctorEntity(request);
        doctor.setUser(savedUser);
        Doctor savedDoctor = doctorRepository.save(doctor);

        RegisterDoctorDto responseDto = modelMapper.toRegisterDoctorDto(savedUser);
        responseDto.setWorkingDays(savedDoctor.getWorkingDays());
        return responseDto;
    }

    @Override
    public RegisterPharmacyDto registerPharmacy(RegisterPharmacyDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already in use.");
        }

        User user = modelMapper.toUserEntity(request);
        user.setRole(Role.PHARMACY);
        user.setHashedPassword(passwordEncoder.encode(user.getHashedPassword()));
        User savedUser = userRepository.save(user);

        Pharmacy pharmacy = modelMapper.toPharmacyEntity(request);
        pharmacy.setUser(savedUser);
        Pharmacy savedPharmacy = pharmacyRepository.save(pharmacy);

        RegisterPharmacyDto responseDto =modelMapper.toRegisterPharmacyDto(savedUser);
        responseDto.setPhoneNumber(savedPharmacy.getPhoneNumber());

        return responseDto;
    }
}
