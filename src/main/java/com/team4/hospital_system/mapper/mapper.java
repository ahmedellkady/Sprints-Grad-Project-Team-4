package com.team4.hospital_system.mapper;

import com.team4.hospital_system.dto.request.*;
import com.team4.hospital_system.model.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class mapper {
    private final ModelMapper modelMapper;

    public Patient toPatientEntity(RegisterPatientDto patientDto){
        return modelMapper.map(patientDto, Patient.class);
    }

    public RegisterPatientDto toRegisterPatientDto(User user){
        return modelMapper.map(user, RegisterPatientDto.class);
    }

    public Doctor toDoctorEntity(RegisterDoctorDto doctorDto){
        return modelMapper.map(doctorDto, Doctor.class);
    }

    public RegisterDoctorDto toRegisterDoctorDto(User user){
        return modelMapper.map(user, RegisterDoctorDto.class);
    }

    public Pharmacy toPharmacyEntity(RegisterPharmacyDto pharmacyDto){
        return modelMapper.map(pharmacyDto, Pharmacy.class);
    }

    public RegisterPharmacyDto toRegisterPharmacyDto(User user){
        return modelMapper.map(user, RegisterPharmacyDto.class);
    }

    public User toUserEntity(RegisterPatientDto Dto){
        return modelMapper.map(Dto, User.class);
    }

    public User toUserEntity(RegisterDoctorDto Dto){
        return modelMapper.map(Dto, User.class);
    }

    public User toUserEntity(RegisterPharmacyDto Dto){
        return modelMapper.map(Dto, User.class);
    }

}
