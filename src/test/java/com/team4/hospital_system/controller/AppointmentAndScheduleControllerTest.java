package com.team4.hospital_system.controller;

import com.team4.hospital_system.dto.request.AppointmentRequestDto;
import com.team4.hospital_system.dto.response.AppointmentResponseDto;
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
        AppointmentRequestDto request = new AppointmentRequestDto();
        request.setDoctorId(1L);
        request.setPatientId(2L);
        request.setAppointmentTime(LocalDateTime.of(2025, 9, 1, 10, 0));

        AppointmentResponseDto response = new AppointmentResponseDto();
        response.setAppointmentId(100L);
        response.setDoctorId(1L);
        response.setPatientId(2L);
        response.setAppointmentTime(request.getAppointmentTime());

        when(appointmentService.bookAppointment(request)).thenReturn(response);

        AppointmentResponseDto result = appointmentController.bookAppointment(request);

        assertNotNull(result);
        assertEquals(100L, result.getAppointmentId());
        assertEquals(1L, result.getDoctorId());
        assertEquals(2L, result.getPatientId());
        verify(appointmentService, times(1)).bookAppointment(request);
    }

    // 2. View Doctor Schedule
    @Test
    void testViewDoctorSchedule() {
        DoctorScheduleDto schedule = new DoctorScheduleDto();
        schedule.setDoctorId(1L);
        schedule.setAvailableSlots(Arrays.asList(
                LocalDateTime.of(2025, 9, 1, 9, 0),
                LocalDateTime.of(2025, 9, 1, 11, 0)
        ));

        when(scheduleService.getDoctorSchedule(1L)).thenReturn(schedule);

        DoctorScheduleDto result = scheduleController.getDoctorSchedule(1L);

        assertNotNull(result);
        assertEquals(1L, result.getDoctorId());
        assertEquals(2, result.getAvailableSlots().size());
        verify(scheduleService, times(1)).getDoctorSchedule(1L);
    }

    // 3. View Appointments
    @Test
    void testViewAppointments() {
        AppointmentResponseDto appt1 = new AppointmentResponseDto();
        appt1.setAppointmentId(100L);

        AppointmentResponseDto appt2 = new AppointmentResponseDto();
        appt2.setAppointmentId(101L);

        List<AppointmentResponseDto> appointments = Arrays.asList(appt1, appt2);

        when(appointmentService.getAllAppointments()).thenReturn(appointments);

        List<AppointmentResponseDto> result = appointmentController.getAllAppointments();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(appointmentService, times(1)).getAllAppointments();
    }
}
