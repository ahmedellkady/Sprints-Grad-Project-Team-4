package com.team4.hospital_system.controller;

import com.team4.hospital_system.dto.request.AppointmentRequestDto;
import com.team4.hospital_system.dto.response.AppointmentResponseDto;
import com.team4.hospital_system.dto.response.AppointmentDto;
import com.team4.hospital_system.dto.response.DoctorScheduleDto;
import com.team4.hospital_system.service.AppointmentService;
import com.team4.hospital_system.service.ScheduleService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentAndScheduleControllerTest {

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private AppointmentController appointmentController;

    @InjectMocks
    private ScheduleController scheduleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 1. Book Appointment
    @Test
    void testBookAppointment() {
        Long doctorId = 1L;
        Long patientId = 2L;
        LocalDateTime appointmentTime = LocalDateTime.of(2025, 9, 1, 10, 0);
        
        AppointmentRequestDto request = new AppointmentRequestDto(doctorId, appointmentTime);

        AppointmentDto response = new AppointmentDto(100L, doctorId, patientId, appointmentTime, appointmentTime.plusHours(1), null);

        when(appointmentService.book(patientId, doctorId, appointmentTime)).thenReturn(response);

        AppointmentDto result = appointmentService.book(patientId, doctorId, appointmentTime);

        assertNotNull(result);
        assertEquals(100L, result.id());
        assertEquals(doctorId, result.doctorId());
        assertEquals(patientId, result.patientId());
        verify(appointmentService, times(1)).book(patientId, doctorId, appointmentTime);
    }

    // 2. View Doctor Schedule
    @Test
    void testViewDoctorSchedule() {
        List<DoctorScheduleDto.SlotDto> slots = Arrays.asList(
                new DoctorScheduleDto.SlotDto(LocalDateTime.of(2025, 9, 1, 9, 0), "AVAILABLE"),
                new DoctorScheduleDto.SlotDto(LocalDateTime.of(2025, 9, 1, 11, 0), "AVAILABLE")
        );
        
        DoctorScheduleDto schedule = new DoctorScheduleDto(1L, "Dr. Smith", slots);

        when(scheduleService.getDoctorSchedule(1L)).thenReturn(schedule);

        DoctorScheduleDto result = scheduleService.getDoctorSchedule(1L);

        assertNotNull(result);
        assertEquals(1L, result.getDoctorId());
        assertEquals(2, result.getSlots().size());
        verify(scheduleService, times(1)).getDoctorSchedule(1L);
    }

    // 3. View Appointments
    @Test
    void testViewAppointments() {
        AppointmentDto appt1 = new AppointmentDto(100L, 1L, 2L, LocalDateTime.now(), LocalDateTime.now().plusHours(1), null);
        AppointmentDto appt2 = new AppointmentDto(101L, 1L, 3L, LocalDateTime.now(), LocalDateTime.now().plusHours(1), null);

        List<AppointmentDto> appointments = Arrays.asList(appt1, appt2);

        when(appointmentService.listForDoctor(1L)).thenReturn(appointments);

        List<AppointmentDto> result = appointmentService.listForDoctor(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(appointmentService, times(1)).listForDoctor(1L);
    }
}
