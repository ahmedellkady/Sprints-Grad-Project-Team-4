package com.team4.hospital_system;

import com.team4.hospital_system.dto.response.UserDto;
import com.team4.hospital_system.exception.BadRequestException;
import com.team4.hospital_system.exception.ResourceNotFoundException;
import com.team4.hospital_system.model.*;
import com.team4.hospital_system.model.enums.Role;
import com.team4.hospital_system.repository.DoctorRepository;
import com.team4.hospital_system.repository.PatientRepository;
import com.team4.hospital_system.repository.PharmacyRepository;
import com.team4.hospital_system.repository.UserRepository;
import com.team4.hospital_system.service.Impl.AdminServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PharmacyRepository pharmacyRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    private User adminUser;
    private User patientUser;
    private User doctorUser;
    private User pharmacyUser;
    private Patient patient;
    private Doctor doctor;
    private Pharmacy pharmacy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup test data
        adminUser = new User();
        adminUser.setId(1L);
        adminUser.setName("Admin User");
        adminUser.setEmail("admin@hospital.com");
        adminUser.setRole(Role.ADMIN);

        patientUser = new User();
        patientUser.setId(2L);
        patientUser.setName("John Doe");
        patientUser.setEmail("john@example.com");
        patientUser.setRole(Role.PATIENT);

        doctorUser = new User();
        doctorUser.setId(3L);
        doctorUser.setName("Dr. Smith");
        doctorUser.setEmail("dr.smith@hospital.com");
        doctorUser.setRole(Role.DOCTOR);

        pharmacyUser = new User();
        pharmacyUser.setId(4L);
        pharmacyUser.setName("City Pharmacy");
        pharmacyUser.setEmail("pharmacy@example.com");
        pharmacyUser.setRole(Role.PHARMACY);

        patient = new Patient();
        patient.setId(2L); // Same ID as patientUser
        patient.setUser(patientUser);

        doctor = new Doctor();
        doctor.setId(3L); // Same ID as doctorUser
        doctor.setUser(doctorUser);

        pharmacy = new Pharmacy();
        pharmacy.setId(4L); // Same ID as pharmacyUser
        pharmacy.setUser(pharmacyUser);
    }

    @Test
    void testListUsers_AllUsers() {
        List<User> users = Arrays.asList(adminUser, patientUser, doctorUser, pharmacyUser);
        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> result = adminService.listUsers(null);

        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals("Admin User", result.get(0).getName());
        assertEquals("ADMIN", result.get(0).getRole());
        assertEquals("John Doe", result.get(1).getName());
        assertEquals("PATIENT", result.get(1).getRole());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testListUsers_ByRole() {
        List<User> patients = Arrays.asList(patientUser);
        when(userRepository.findByRole(Role.PATIENT)).thenReturn(patients);

        List<UserDto> result = adminService.listUsers("PATIENT");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("PATIENT", result.get(0).getRole());
        verify(userRepository, times(1)).findByRole(Role.PATIENT);
    }

    @Test
    void testListUsers_InvalidRole() {
        assertThrows(BadRequestException.class, () -> {
            adminService.listUsers("INVALID_ROLE");
        });
        verify(userRepository, never()).findByRole(any());
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(patientUser));

        UserDto result = adminService.getUserById(2L);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("PATIENT", result.getRole());
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            adminService.getUserById(99L);
        });
        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    void testDeleteUser_Patient() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(patientUser));
        when(patientRepository.findById(2L)).thenReturn(Optional.of(patient));

        adminService.deleteUser(1L, 2L);

        verify(patientRepository, times(1)).delete(patient);
        verify(userRepository, times(1)).delete(patientUser);
    }

    @Test
    void testDeleteUser_Doctor() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(3L)).thenReturn(Optional.of(doctorUser));
        when(doctorRepository.findById(3L)).thenReturn(Optional.of(doctor));

        adminService.deleteUser(1L, 3L);

        verify(doctorRepository, times(1)).delete(doctor);
        verify(userRepository, times(1)).delete(doctorUser);
    }

    @Test
    void testDeleteUser_Pharmacy() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(4L)).thenReturn(Optional.of(pharmacyUser));
        when(pharmacyRepository.findById(4L)).thenReturn(Optional.of(pharmacy));

        adminService.deleteUser(1L, 4L);

        verify(pharmacyRepository, times(1)).delete(pharmacy);
        verify(userRepository, times(1)).delete(pharmacyUser);
    }

    @Test
    void testDeleteUser_AdminNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            adminService.deleteUser(99L, 2L);
        });
        verify(userRepository, never()).delete(any());
    }

    @Test
    void testDeleteUser_NotAdmin() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(patientUser));

        assertThrows(BadRequestException.class, () -> {
            adminService.deleteUser(2L, 3L);
        });
        verify(userRepository, never()).delete(any());
    }

    @Test
    void testDeleteUser_DeleteSelf() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));

        assertThrows(BadRequestException.class, () -> {
            adminService.deleteUser(1L, 1L);
        });
        verify(userRepository, never()).delete(any());
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            adminService.deleteUser(1L, 99L);
        });
        verify(userRepository, never()).delete(any());
    }

    @Test
    void testDeleteUser_CannotDeleteAdmin() {
        User anotherAdmin = new User();
        anotherAdmin.setId(5L);
        anotherAdmin.setRole(Role.ADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(5L)).thenReturn(Optional.of(anotherAdmin));

        assertThrows(BadRequestException.class, () -> {
            adminService.deleteUser(1L, 5L);
        });
        verify(userRepository, never()).delete(any());
    }
}
