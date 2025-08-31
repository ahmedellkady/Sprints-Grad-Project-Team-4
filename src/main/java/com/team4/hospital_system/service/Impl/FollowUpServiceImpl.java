package com.team4.hospital_system.service.Impl;

import com.team4.hospital_system.dto.request.FollowUpRequestDto;
import com.team4.hospital_system.dto.response.FollowUpResponseDto;
import com.team4.hospital_system.exception.ResourceNotFoundException;
import com.team4.hospital_system.model.Doctor;
import com.team4.hospital_system.model.Patient;
import com.team4.hospital_system.repository.DoctorRepository;
import com.team4.hospital_system.repository.PatientRepository;
import com.team4.hospital_system.service.FollowUpService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FollowUpServiceImpl implements FollowUpService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public FollowUpServiceImpl(DoctorRepository doctorRepository,
                              PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public FollowUpResponseDto createFollowUp(Long doctorId, FollowUpRequestDto request) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + doctorId));
        
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + request.patientId()));

        // Update patient status with follow-up information
        String followUpInfo = String.format("Status: %s | Notes: %s | Follow-up Date: %s | Doctor: %s",
                request.patientStatus(),
                request.notes(),
                request.followUpDate().toString(),
                doctor.getUser().getName());
        
        patient.setStatus(followUpInfo);
        patient = patientRepository.save(patient);

        return new FollowUpResponseDto(
                patient.getId(), // Using patient ID as follow-up ID
                doctorId,
                doctor.getUser().getName(),
                patient.getId(),
                patient.getUser().getName(),
                request.notes(),
                request.patientStatus(),
                request.followUpDate(),
                LocalDateTime.now()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<FollowUpResponseDto> getPatientFollowUps(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + patientId));
        
        // Since we're storing follow-up info in patient status, we return current status as follow-up
        if (patient.getStatus() != null && !patient.getStatus().isEmpty()) {
            return List.of(createFollowUpFromPatientStatus(patient));
        }
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FollowUpResponseDto> getDoctorFollowUps(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + doctorId));
        
        // Get all patients and filter those with follow-up information
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .filter(p -> p.getStatus() != null && p.getStatus().contains("Doctor: " + doctor.getUser().getName()))
                .map(this::createFollowUpFromPatientStatus)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FollowUpResponseDto> getFollowUpsByDoctorAndPatient(Long doctorId, Long patientId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + doctorId));
        
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + patientId));
        
        if (patient.getStatus() != null && patient.getStatus().contains("Doctor: " + doctor.getUser().getName())) {
            return List.of(createFollowUpFromPatientStatus(patient));
        }
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public FollowUpResponseDto getFollowUpById(Long followUpId) {
        // Since we're using patient ID as follow-up ID, we get the patient
        Patient patient = patientRepository.findById(followUpId)
                .orElseThrow(() -> new ResourceNotFoundException("Follow-up not found: " + followUpId));
        
        if (patient.getStatus() == null || patient.getStatus().isEmpty()) {
            throw new ResourceNotFoundException("No follow-up information found for patient: " + followUpId);
        }
        
        return createFollowUpFromPatientStatus(patient);
    }

    private FollowUpResponseDto createFollowUpFromPatientStatus(Patient patient) {
        // Parse the status string to extract follow-up information
        String status = patient.getStatus();
        
        // Extract information from the status string
        String patientStatus = extractValue(status, "Status: ");
        String notes = extractValue(status, "Notes: ");
        String followUpDateStr = extractValue(status, "Follow-up Date: ");
        String doctorName = extractValue(status, "Doctor: ");
        
        LocalDateTime followUpDate = LocalDateTime.now(); // Default value
        try {
            if (followUpDateStr != null) {
                followUpDate = LocalDateTime.parse(followUpDateStr);
            }
        } catch (Exception e) {
            // Keep default value if parsing fails
        }
        
        return new FollowUpResponseDto(
                patient.getId(),
                null, // We don't have doctor ID in this simplified approach
                doctorName,
                patient.getId(),
                patient.getUser().getName(),
                notes,
                patientStatus,
                followUpDate,
                LocalDateTime.now()
        );
    }

    private String extractValue(String status, String prefix) {
        if (status == null || !status.contains(prefix)) {
            return null;
        }
        
        int startIndex = status.indexOf(prefix) + prefix.length();
        int endIndex = status.indexOf(" | ", startIndex);
        
        if (endIndex == -1) {
            return status.substring(startIndex);
        }
        
        return status.substring(startIndex, endIndex);
    }
}
