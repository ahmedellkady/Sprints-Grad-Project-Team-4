package com.team4.hospital_system.service;

import com.team4.hospital_system.dto.AppointmentDto;
import com.team4.hospital_system.entity.Appointment;
import com.team4.hospital_system.exception.ResourceNotFoundException;
import com.team4.hospital_system.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRescheduleAppointment() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setAppointmentTime(LocalDateTime.now());

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        AppointmentDto result = appointmentService.rescheduleAppointment(1L, LocalDateTime.now().plusDays(2));

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void testCancelAppointment() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setCancelled(false);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        AppointmentDto result = appointmentService.cancelAppointment(1L);

        assertTrue(result.isCancelled());
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void testAcceptAppointment() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setAccepted(false);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        AppointmentDto result = appointmentService.acceptAppointment(1L);

        assertTrue(result.isAccepted());
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }
}
