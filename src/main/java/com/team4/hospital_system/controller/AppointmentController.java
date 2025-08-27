package com.team4.hospital_system.controller;

import com.team4.hospital_system.dto.request.AppointmentRequestDto;
import com.team4.hospital_system.dto.response.AppointmentResponseDto;
import com.team4.hospital_system.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // Patient books an appointment with a doctor
    @PostMapping("/book/{patientId}")
    public ResponseEntity<AppointmentResponseDto> book(@PathVariable Long patientId,
                                                       @Valid @RequestBody AppointmentRequestDto request) {
        return ResponseEntity.ok(appointmentService.bookAppointment(patientId, request));
    }

    // View patient's appointments
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponseDto>> getPatientAppointments(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsForPatient(patientId));
    }

    // View a doctor's schedule for a day
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentResponseDto>> getDoctorSchedule(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day) {
        return ResponseEntity.ok(appointmentService.getDoctorSchedule(doctorId, day));
    }
}
