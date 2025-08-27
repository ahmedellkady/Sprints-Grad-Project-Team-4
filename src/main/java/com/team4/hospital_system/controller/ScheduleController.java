package com.team4.hospital_system.controller;

import com.team4.hospital_system.dto.response.DoctorScheduleDto;
import com.team4.hospital_system.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // Patient views a doctor's schedule (next 7 days window by default).
    @GetMapping("/doctors/{doctorId}")
    public ResponseEntity<DoctorScheduleDto> viewDoctorSchedule(@PathVariable long doctorId) {
        return ResponseEntity.ok(scheduleService.getDoctorSchedule(doctorId));
    }
}
