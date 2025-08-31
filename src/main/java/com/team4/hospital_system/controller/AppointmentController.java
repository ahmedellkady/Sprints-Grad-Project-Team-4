package com.team4.hospital_system.controller;

import com.team4.hospital_system.dto.request.AppointmentRequestDto;
import com.team4.hospital_system.dto.response.AppointmentDto;
import com.team4.hospital_system.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/patients/{patientId}/appointments")
    public ResponseEntity<AppointmentDto> book(@PathVariable long patientId,
                                               @Valid @RequestBody AppointmentRequestDto request) {
        var appt = appointmentService.book(patientId, request.doctorId(), request.appointmentTime());
        return ResponseEntity.created(URI.create("/api/appointments/" + appt.id())).body(appt);
    }

    @GetMapping("/patients/{patientId}/appointments")
    public ResponseEntity<List<AppointmentDto>> listForPatient(@PathVariable long patientId) {
        return ResponseEntity.ok(appointmentService.listForPatient(patientId));
    }

    @GetMapping("/doctors/{doctorId}/appointments")
    public ResponseEntity<List<AppointmentDto>> listForDoctor(@PathVariable long doctorId) {
        return ResponseEntity.ok(appointmentService.listForDoctor(doctorId));
    }
}
