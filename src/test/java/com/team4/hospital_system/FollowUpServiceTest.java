package com.team4.hospital_system;

import com.team4.hospital_system.dto.request.FollowUpRequestDto;
import com.team4.hospital_system.dto.response.FollowUpResponseDto;
import com.team4.hospital_system.model.Doctor;
import com.team4.hospital_system.model.Patient;
import com.team4.hospital_system.model.User;
import com.team4.hospital_system.repository.DoctorRepository;
import com.team4.hospital_system.repository.PatientRepository;
import com.team4.hospital_system.service.Impl.FollowUpServiceImpl;
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

class FollowUpServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private FollowUpServiceImpl followUpService;

    private Doctor doctor;
    private Patient patient;
    private User doctorUser;
    private User patientUser;

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
        patient.setStatus("Under Treatment");
    }

    @Test
    void testCreateFollowUp() {
        Long doctorId = 1L;
        FollowUpRequestDto request = new FollowUpRequestDto(
                2L, 
                "Patient showing improvement. Continue current medication.", 
                "Improving", 
                LocalDateTime.now().plusDays(7)
        );

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(2L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        FollowUpResponseDto result = followUpService.createFollowUp(doctorId, request);

        assertNotNull(result);
        assertEquals(2L, result.id()); // Using patient ID as follow-up ID
        assertEquals(doctorId, result.doctorId());
        assertEquals("Dr. Smith", result.doctorName());
        assertEquals(2L, result.patientId());
        assertEquals("John Doe", result.patientName());
        assertEquals("Patient showing improvement. Continue current medication.", result.notes());
        assertEquals("Improving", result.patientStatus());
        
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void testGetPatientFollowUps() {
        Long patientId = 2L;
        patient.setStatus("Status: Improving | Notes: Patient showing improvement | Follow-up Date: 2025-01-15T10:00 | Doctor: Dr. Smith");

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        List<FollowUpResponseDto> result = followUpService.getPatientFollowUps(patientId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).id());
        assertEquals(2L, result.get(0).patientId());
        assertEquals("Improving", result.get(0).patientStatus());
        assertEquals("Patient showing improvement", result.get(0).notes());
        verify(patientRepository, times(1)).findById(patientId);
    }

    @Test
    void testGetPatientFollowUps_NoFollowUp() {
        Long patientId = 2L;
        patient.setStatus(null);

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        List<FollowUpResponseDto> result = followUpService.getPatientFollowUps(patientId);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(patientRepository, times(1)).findById(patientId);
    }

    @Test
    void testGetDoctorFollowUps() {
        Long doctorId = 1L;
        Patient patient1 = new Patient();
        patient1.setId(2L);
        patient1.setUser(patientUser);
        patient1.setStatus("Status: Improving | Notes: Patient showing improvement | Follow-up Date: 2025-01-15T10:00 | Doctor: Dr. Smith");
        
        Patient patient2 = new Patient();
        patient2.setId(3L);
        patient2.setUser(new User());
        patient2.setStatus("Status: Stable | Notes: Patient stable | Follow-up Date: 2025-01-16T10:00 | Doctor: Dr. Smith");

        List<Patient> patients = Arrays.asList(patient1, patient2);

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(patientRepository.findAll()).thenReturn(patients);

        List<FollowUpResponseDto> result = followUpService.getDoctorFollowUps(doctorId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(2L, result.get(0).patientId());
        assertEquals(3L, result.get(1).patientId());
        verify(doctorRepository, times(1)).findById(doctorId);
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void testGetFollowUpsByDoctorAndPatient() {
        Long doctorId = 1L;
        Long patientId = 2L;
        patient.setStatus("Status: Improving | Notes: Patient showing improvement | Follow-up Date: 2025-01-15T10:00 | Doctor: Dr. Smith");

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        List<FollowUpResponseDto> result = followUpService.getFollowUpsByDoctorAndPatient(doctorId, patientId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).patientId());
        assertEquals("Improving", result.get(0).patientStatus());
        verify(doctorRepository, times(1)).findById(doctorId);
        verify(patientRepository, times(1)).findById(patientId);
    }

    @Test
    void testGetFollowUpsByDoctorAndPatient_NoMatch() {
        Long doctorId = 1L;
        Long patientId = 2L;
        patient.setStatus("Status: Improving | Notes: Patient showing improvement | Follow-up Date: 2025-01-15T10:00 | Doctor: Dr. Johnson");

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        List<FollowUpResponseDto> result = followUpService.getFollowUpsByDoctorAndPatient(doctorId, patientId);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(doctorRepository, times(1)).findById(doctorId);
        verify(patientRepository, times(1)).findById(patientId);
    }

    @Test
    void testGetFollowUpById() {
        Long followUpId = 2L;
        patient.setStatus("Status: Improving | Notes: Patient showing improvement | Follow-up Date: 2025-01-15T10:00 | Doctor: Dr. Smith");

        when(patientRepository.findById(followUpId)).thenReturn(Optional.of(patient));

        FollowUpResponseDto result = followUpService.getFollowUpById(followUpId);

        assertNotNull(result);
        assertEquals(followUpId, result.id());
        assertEquals(2L, result.patientId());
        assertEquals("Improving", result.patientStatus());
        assertEquals("Patient showing improvement", result.notes());
        verify(patientRepository, times(1)).findById(followUpId);
    }
}
