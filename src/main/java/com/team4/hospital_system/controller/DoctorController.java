package com.team4.hospital_system.controller;

import com.team4.hospital_system.dto.response.PatientHistoryDto;
import com.team4.hospital_system.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // === Use Case 3: Doctor views patient history ===
    @GetMapping("/{doctorId}/patients/{patientId}/history")
    public ResponseEntity<List<PatientHistoryDto>> getPatientHistory(
            @PathVariable Long doctorId,
            @PathVariable Long patientId) {

        List<PatientHistoryDto> history = doctorService.getPatientHistory(doctorId, patientId);
        return ResponseEntity.ok(history);
    }
}
