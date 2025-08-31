package com.team4.hospital_system;

import com.team4.hospital_system.dto.response.AppointmentDto;
import com.team4.hospital_system.model.Appointment;
import com.team4.hospital_system.model.Doctor;
import com.team4.hospital_system.model.Patient;
import com.team4.hospital_system.model.User;
import com.team4.hospital_system.model.enums.AppointmentStatus;
import com.team4.hospital_system.repository.AppointmentRepository;
import com.team4.hospital_system.repository.DoctorRepository;
import com.team4.hospital_system.repository.PatientRepository;
import com.team4.hospital_system.service.Impl.AppointmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private Doctor doctor;
    private Patient patient;
    private User doctorUser;
    private User patientUser;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup test data
        doctorUser = new User();
        doctorUser.setId(1L);
        doctorUser.setName("Dr. Smith");
        
        patientUser = new User();
        patientUser.setId(2L);
        patientUser.setName("John Doe");
        
        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setUser(doctorUser);
        
        patient = new Patient();
        patient.setId(2L);
        patient.setUser(patientUser);
        
        appointment = Appointment.builder()
                .id(1L)
                .doctor(doctor)
                .patient(patient)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusMinutes(30))
                .status(AppointmentStatus.BOOKED)
                .build();
    }

    @Test
    void testBookAppointment() {
        Long doctorId = 1L;
        Long patientId = 2L;
        LocalDateTime appointmentTime = LocalDateTime.now().plusDays(1);
        
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findDoctorOverlaps(doctorId, appointmentTime, appointmentTime.plusMinutes(30)))
                .thenReturn(List.of());
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        AppointmentDto result = appointmentService.book(patientId, doctorId, appointmentTime);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(doctorId, result.doctorId());
        assertEquals(patientId, result.patientId());
        assertEquals(AppointmentStatus.BOOKED, result.status());
        
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void testListForPatient() {
        Long patientId = 2L;
        List<Appointment> appointments = Arrays.asList(appointment);

        when(appointmentRepository.findByPatientId(patientId)).thenReturn(appointments);

        List<AppointmentDto> result = appointmentService.listForPatient(patientId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(2L, result.get(0).patientId());
        verify(appointmentRepository, times(1)).findByPatientId(patientId);
    }

    @Test
    void testListForDoctor() {
        Long doctorId = 1L;
        List<Appointment> appointments = Arrays.asList(appointment);

        when(appointmentRepository.findByDoctorId(doctorId)).thenReturn(appointments);

        List<AppointmentDto> result = appointmentService.listForDoctor(doctorId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(doctorId, result.get(0).doctorId());
        verify(appointmentRepository, times(1)).findByDoctorId(doctorId);
    }
}
