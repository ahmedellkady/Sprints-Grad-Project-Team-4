package com.team4.hospital_system.service;

import com.team4.hospital_system.dto.request.*;
import com.team4.hospital_system.dto.response.AuthResponseDto;


public interface AuthService {

    AuthResponseDto login(LoginDto request);

    RegisterPatientDto registerPatient(RegisterPatientDto request);

    RegisterDoctorDto registerDoctor(RegisterDoctorDto request);

    RegisterPharmacyDto registerPharmacy(RegisterPharmacyDto request);
}
