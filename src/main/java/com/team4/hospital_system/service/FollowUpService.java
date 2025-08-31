package com.team4.hospital_system.service;

import com.team4.hospital_system.dto.request.FollowUpRequestDto;
import com.team4.hospital_system.dto.response.FollowUpResponseDto;

import java.util.List;

public interface FollowUpService {
    FollowUpResponseDto createFollowUp(Long doctorId, FollowUpRequestDto request);
    List<FollowUpResponseDto> getPatientFollowUps(Long patientId);
    List<FollowUpResponseDto> getDoctorFollowUps(Long doctorId);
    List<FollowUpResponseDto> getFollowUpsByDoctorAndPatient(Long doctorId, Long patientId);
    FollowUpResponseDto getFollowUpById(Long followUpId);
}
