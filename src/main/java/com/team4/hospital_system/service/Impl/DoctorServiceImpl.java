package com.team4.hospital_system.service.impl;

import com.team4.hospital_system.dto.response.PatientHistoryDto;
import com.team4.hospital_system.exception.ResourceNotFoundException;
import com.team4.hospital_system.model.Patient;
import com.team4.hospital_system.repository.PatientRepository;
import com.team4.hospital_system.service.DoctorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final PatientRepository patientRepository;

    public DoctorServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public List<PatientHistoryDto> getPatientHistory(Long doctorId, Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        // ⚠️ Optionally, validate that doctorId has access to this patient before returning

        return patient.getHistory().stream()
                .map(history -> new PatientHistoryDto(
                        history.getId(),
                        history.getDescription(),
                        history.getDate()
                ))
                .toList();
    }
}

