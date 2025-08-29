package com.team4.hospital_system.controller;

import com.team4.hospital_system.dto.request.AddPrescriptionItemDto;
import com.team4.hospital_system.dto.request.CreatePrescriptionDto;
import com.team4.hospital_system.dto.response.PrescriptionDto;
import com.team4.hospital_system.service.PrescriptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping("/doctors/{doctorId}/patients/{patientId}")
    public ResponseEntity<PrescriptionDto> create(@PathVariable long doctorId,
                                                  @PathVariable long patientId,
                                                  @Valid @RequestBody CreatePrescriptionDto request) {
        PrescriptionDto created = prescriptionService.create(doctorId, patientId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/doctors/{doctorId}/{prescriptionId}/items")
    public ResponseEntity<PrescriptionDto> addItem(@PathVariable long doctorId,
                                                   @PathVariable long prescriptionId,
                                                   @Valid @RequestBody AddPrescriptionItemDto request) {
        return ResponseEntity.ok(prescriptionService.addItem(doctorId, prescriptionId, request));
    }

    @GetMapping("/patients/{patientId}")
    public ResponseEntity<List<PrescriptionDto>> listForPatient(@PathVariable long patientId) {
        return ResponseEntity.ok(prescriptionService.listForPatient(patientId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionDto> getById(@PathVariable long id) {
        return ResponseEntity.ok(prescriptionService.getById(id));
    }
}


