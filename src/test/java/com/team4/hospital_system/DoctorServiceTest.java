package com.team4.hospital_system;

import com.team4.hospital_system.dto.response.PatientHistoryDto;
import com.team4.hospital_system.exception.ResourceNotFoundException;
import com.team4.hospital_system.model.Patient;
import com.team4.hospital_system.model.User;
import com.team4.hospital_system.repository.PatientRepository;
import com.team4.hospital_system.service.Impl.DoctorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private Patient patient;
    private User patientUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup test data
        patientUser = new User();
        patientUser.setId(2L);
        patientUser.setName("John Doe");
        
        patient = new Patient();
        patient.setId(2L);
        patient.setUser(patientUser);
    }

    @Test
    void testGetPatientHistory_Success() {
        Long doctorId = 1L;
        Long patientId = 2L;
        
        // Mock the repository
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        
        // Call the service method
        List<PatientHistoryDto> history = doctorService.getPatientHistory(doctorId, patientId);

        assertNotNull(history);
        // Currently returns empty list as per implementation
        assertEquals(0, history.size());
        verify(patientRepository, times(1)).findById(patientId);
    }

    @Test
    void testGetPatientHistory_PatientNotFound() {
        Long doctorId = 1L;
        Long patientId = 99L;
        
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            doctorService.getPatientHistory(doctorId, patientId);
        });
        
        verify(patientRepository, times(1)).findById(patientId);
    }
}
