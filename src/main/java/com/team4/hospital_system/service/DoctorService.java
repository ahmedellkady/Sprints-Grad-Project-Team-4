package com.team4.hospital_system.service;

import com.team4.hospital_system.dto.response.PatientHistoryDto;

import java.util.List;

public interface DoctorService {
    List<PatientHistoryDto> getPatientHistory(Long doctorId, Long patientId);
}
