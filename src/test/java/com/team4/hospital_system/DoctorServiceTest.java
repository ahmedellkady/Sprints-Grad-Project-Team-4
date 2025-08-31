package com.team4.hospital_system.service.impl;

import com.team4.hospital_system.dto.response.PatientHistoryDto;
import com.team4.hospital_system.exception.ResourceNotFoundException;
import com.team4.hospital_system.model.Patient;
import com.team4.hospital_system.repository.PatientRepository;
import com.team4.hospital_system.service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private Patient patient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a dummy patient with a history
        patient = new Patient();
        patient.setId(1L);

        // Assume patient has some medical history
        patient.setHistory(List.of(
                new PatientHistoryDto("Flu", "Recovered", "2022-05-01"),
                new PatientHistoryDto("Allergy", "Under treatment", "2023-03-15")
        ));
    }

    @Test
    void getPatientHistory_success() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        List<PatientHistoryDto> history = doctorService.getPatientHistory(10L, 1L);

        assertNotNull(history);
        assertEquals(2, history.size());
        assertEquals("Flu", history.get(0).condition());
        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    void getPatientHistory_patientNotFound() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            doctorService.getPatientHistory(10L, 99L);
        });
    }
}
