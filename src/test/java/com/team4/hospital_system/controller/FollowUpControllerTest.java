package com.team4.hospital_system.controller;

import com.team4.hospital_system.dto.request.FollowUpRequestDto;
import com.team4.hospital_system.dto.response.FollowUpResponseDto;
import com.team4.hospital_system.service.FollowUpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FollowUpControllerTest {

    @Mock
    private FollowUpService followUpService;

    @InjectMocks
    private FollowUpController followUpController;

    private FollowUpRequestDto requestDto;
    private FollowUpResponseDto responseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        requestDto = new FollowUpRequestDto(
                2L,
                "Patient showing improvement. Continue current medication.",
                "Improving",
                LocalDateTime.now().plusDays(7)
        );
        
        responseDto = new FollowUpResponseDto(
                1L,
                1L,
                "Dr. Smith",
                2L,
                "John Doe",
                "Patient showing improvement. Continue current medication.",
                "Improving",
                LocalDateTime.now().plusDays(7),
                LocalDateTime.now()
        );
    }

    @Test
    void testCreateFollowUp() {
        Long doctorId = 1L;
        
        when(followUpService.createFollowUp(doctorId, requestDto)).thenReturn(responseDto);

        ResponseEntity<FollowUpResponseDto> response = followUpController.createFollowUp(doctorId, requestDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id());
        assertEquals(doctorId, response.getBody().doctorId());
        assertEquals("Dr. Smith", response.getBody().doctorName());
        
        verify(followUpService, times(1)).createFollowUp(doctorId, requestDto);
    }

    @Test
    void testGetDoctorFollowUps() {
        Long doctorId = 1L;
        List<FollowUpResponseDto> followUps = Arrays.asList(responseDto);
        
        when(followUpService.getDoctorFollowUps(doctorId)).thenReturn(followUps);

        ResponseEntity<List<FollowUpResponseDto>> response = followUpController.getDoctorFollowUps(doctorId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(doctorId, response.getBody().get(0).doctorId());
        
        verify(followUpService, times(1)).getDoctorFollowUps(doctorId);
    }

    @Test
    void testGetPatientFollowUpsByDoctor() {
        Long doctorId = 1L;
        Long patientId = 2L;
        List<FollowUpResponseDto> followUps = Arrays.asList(responseDto);
        
        when(followUpService.getFollowUpsByDoctorAndPatient(doctorId, patientId)).thenReturn(followUps);

        ResponseEntity<List<FollowUpResponseDto>> response = followUpController.getPatientFollowUpsByDoctor(doctorId, patientId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(doctorId, response.getBody().get(0).doctorId());
        assertEquals(patientId, response.getBody().get(0).patientId());
        
        verify(followUpService, times(1)).getFollowUpsByDoctorAndPatient(doctorId, patientId);
    }

    @Test
    void testGetPatientFollowUps() {
        Long patientId = 2L;
        List<FollowUpResponseDto> followUps = Arrays.asList(responseDto);
        
        when(followUpService.getPatientFollowUps(patientId)).thenReturn(followUps);

        ResponseEntity<List<FollowUpResponseDto>> response = followUpController.getPatientFollowUps(patientId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(patientId, response.getBody().get(0).patientId());
        
        verify(followUpService, times(1)).getPatientFollowUps(patientId);
    }

    @Test
    void testGetFollowUpById() {
        Long followUpId = 1L;
        
        when(followUpService.getFollowUpById(followUpId)).thenReturn(responseDto);

        ResponseEntity<FollowUpResponseDto> response = followUpController.getFollowUpById(followUpId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(followUpId, response.getBody().id());
        
        verify(followUpService, times(1)).getFollowUpById(followUpId);
    }
}
