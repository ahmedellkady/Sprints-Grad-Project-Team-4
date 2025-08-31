package com.team4.hospital_system.controller;

import com.team4.hospital_system.dto.request.AppointmentRequestDto;
import com.team4.hospital_system.dto.request.RescheduleAppointmentRequest;
import com.team4.hospital_system.dto.response.AppointmentDto;
import com.team4.hospital_system.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // === Book appointment (patient initiates) ===
    @PostMapping("/patients/{patientId}")
    public ResponseEntity<AppointmentDto> book(@PathVariable long patientId,
                                               @Valid @RequestBody AppointmentRequestDto request) {
        AppointmentDto appt = appointmentService.book(patientId, request.doctorId(), request.appointmentTime());
        return ResponseEntity.created(URI.create("/api/appointments/" + appt.id())).body(appt);
    }

    // === List appointments for a patient ===
    @GetMapping("/patients/{patientId}")
    public ResponseEntity<List<AppointmentDto>> listForPatient(@PathVariable long patientId) {
        return ResponseEntity.ok(appointmentService.listForPatient(patientId));
    }

    // === List appointments for a doctor ===
    @GetMapping("/doctors/{doctorId}")
    public ResponseEntity<List<AppointmentDto>> listForDoctor(@PathVariable long doctorId) {
        return ResponseEntity.ok(appointmentService.listForDoctor(doctorId));
    }

    // === Use Case 1: Reschedule Appointment ===
    @PutMapping("/{appointmentId}/reschedule")
    public ResponseEntity<AppointmentDto> rescheduleAppointment(
            @PathVariable Long appointmentId,
            @RequestBody @Valid RescheduleAppointmentRequest request) {
        AppointmentDto updated = appointmentService.rescheduleAppointment(appointmentId, request);
        return ResponseEntity.ok(updated);
    }

    // === Use Case 1: Cancel Appointment ===
    @DeleteMapping("/{appointmentId}/cancel")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.noContent().build();
    }

    // === Use Case 2: Accept Appointment ===
    @PutMapping("/{appointmentId}/accept")
    public ResponseEntity<AppointmentDto> acceptAppointment(@PathVariable Long appointmentId) {
        AppointmentDto accepted = appointmentService.acceptAppointment(appointmentId);
        return ResponseEntity.ok(accepted);
    }
}
