package com.team4.hospital_system.controller;

import com.team4.hospital_system.dto.response.UserDto;
import com.team4.hospital_system.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    private List<UserDto> users;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        users = Arrays.asList(
                UserDto.builder()
                        .id(1L)
                        .name("Admin User")
                        .email("admin@hospital.com")
                        .role("ADMIN")
                        .build(),
                UserDto.builder()
                        .id(2L)
                        .name("John Doe")
                        .email("john@example.com")
                        .role("PATIENT")
                        .build(),
                UserDto.builder()
                        .id(3L)
                        .name("Dr. Smith")
                        .email("dr.smith@hospital.com")
                        .role("DOCTOR")
                        .build()
        );
    }

    @Test
    void testListUsers_AllUsers() {
        when(adminService.listUsers(null)).thenReturn(users);

        ResponseEntity<List<UserDto>> response = adminController.listUsers(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        assertEquals("Admin User", response.getBody().get(0).getName());
        assertEquals("ADMIN", response.getBody().get(0).getRole());
        verify(adminService, times(1)).listUsers(null);
    }

    @Test
    void testListUsers_ByRole() {
        List<UserDto> patients = Arrays.asList(users.get(1)); // Only patient
        when(adminService.listUsers("PATIENT")).thenReturn(patients);

        ResponseEntity<List<UserDto>> response = adminController.listUsers("PATIENT");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("John Doe", response.getBody().get(0).getName());
        assertEquals("PATIENT", response.getBody().get(0).getRole());
        verify(adminService, times(1)).listUsers("PATIENT");
    }

    @Test
    void testDeleteUser_Success() {
        long adminId = 1L;
        long userId = 2L;

        ResponseEntity<Void> response = adminController.deleteUser(adminId, userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(adminService, times(1)).deleteUser(adminId, userId);
    }

    @Test
    void testGetUserById_Success() {
        long userId = 2L;
        UserDto expectedUser = users.get(1); // John Doe
        when(adminService.getUserById(userId)).thenReturn(expectedUser);

        ResponseEntity<UserDto> response = adminController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userId, response.getBody().getId());
        assertEquals("John Doe", response.getBody().getName());
        assertEquals("john@example.com", response.getBody().getEmail());
        assertEquals("PATIENT", response.getBody().getRole());
        verify(adminService, times(1)).getUserById(userId);
    }
}
