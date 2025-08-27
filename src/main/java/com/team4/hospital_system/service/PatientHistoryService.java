package com.team4.hospital_system.service;

import com.team4.hospital_system.dto.response.PatientHistoryDto;

public interface PatientHistoryService {
    PatientHistoryDto getHistoryForDoctor(long patientId);
}
