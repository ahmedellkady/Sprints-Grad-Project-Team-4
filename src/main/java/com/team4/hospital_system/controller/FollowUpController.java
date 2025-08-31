package com.team4.hospital_system.controller;

import com.team4.hospital_system.dto.request.FollowUpRequestDto;
import com.team4.hospital_system.dto.response.FollowUpResponseDto;
import com.team4.hospital_system.service.FollowUpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follow-ups")
public class FollowUpController {

    private final FollowUpService followUpService;

    public FollowUpController(FollowUpService followUpService) {
        this.followUpService = followUpService;
    }

    // === Use Case 5: Doctor records follow-up notes and updates patient status ===
    @PostMapping("/doctors/{doctorId}")
    public ResponseEntity<FollowUpResponseDto> createFollowUp(
            @PathVariable Long doctorId,
            @RequestBody FollowUpRequestDto request) {
        
        FollowUpResponseDto followUp = followUpService.createFollowUp(doctorId, request);
        return ResponseEntity.ok(followUp);
    }

    @GetMapping("/doctors/{doctorId}")
    public ResponseEntity<List<FollowUpResponseDto>> getDoctorFollowUps(
            @PathVariable Long doctorId) {
        
        List<FollowUpResponseDto> followUps = followUpService.getDoctorFollowUps(doctorId);
        return ResponseEntity.ok(followUps);
    }

    @GetMapping("/doctors/{doctorId}/patients/{patientId}")
    public ResponseEntity<List<FollowUpResponseDto>> getPatientFollowUpsByDoctor(
            @PathVariable Long doctorId,
            @PathVariable Long patientId) {
        
        List<FollowUpResponseDto> followUps = followUpService.getFollowUpsByDoctorAndPatient(doctorId, patientId);
        return ResponseEntity.ok(followUps);
    }

    @GetMapping("/patients/{patientId}")
    public ResponseEntity<List<FollowUpResponseDto>> getPatientFollowUps(
            @PathVariable Long patientId) {
        
        List<FollowUpResponseDto> followUps = followUpService.getPatientFollowUps(patientId);
        return ResponseEntity.ok(followUps);
    }

    @GetMapping("/{followUpId}")
    public ResponseEntity<FollowUpResponseDto> getFollowUpById(
            @PathVariable Long followUpId) {
        
        FollowUpResponseDto followUp = followUpService.getFollowUpById(followUpId);
        return ResponseEntity.ok(followUp);
    }
}
