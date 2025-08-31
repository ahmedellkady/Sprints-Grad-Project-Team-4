package com.team4.hospital_system.service.Impl;

import com.team4.hospital_system.dto.response.UserDto;
import com.team4.hospital_system.exception.BadRequestException;
import com.team4.hospital_system.exception.ResourceNotFoundException;
import com.team4.hospital_system.model.Doctor;
import com.team4.hospital_system.model.Patient;
import com.team4.hospital_system.model.Pharmacy;
import com.team4.hospital_system.model.User;
import com.team4.hospital_system.model.enums.Role;
import com.team4.hospital_system.repository.DoctorRepository;
import com.team4.hospital_system.repository.PatientRepository;
import com.team4.hospital_system.repository.PharmacyRepository;
import com.team4.hospital_system.repository.UserRepository;
import com.team4.hospital_system.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PharmacyRepository pharmacyRepository;

    public AdminServiceImpl(UserRepository userRepository,
                           PatientRepository patientRepository,
                           DoctorRepository doctorRepository,
                           PharmacyRepository pharmacyRepository) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.pharmacyRepository = pharmacyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> listUsers(String role) {
        List<User> users;
        
        if (role != null && !role.trim().isEmpty()) {
            try {
                Role userRole = Role.valueOf(role.toUpperCase());
                users = userRepository.findByRole(userRole);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid role: " + role);
            }
        } else {
            users = userRepository.findAll();
        }
        
        return users.stream()
                .map(this::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        
        return toUserDto(user);
    }

    @Override
    public void deleteUser(long adminId, long userId) {
        // Validate admin exists and has admin role
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found: " + adminId));
        
        if (admin.getRole() != Role.ADMIN) {
            throw new BadRequestException("Only admins can delete users");
        }
        
        // Prevent admin from deleting themselves
        if (adminId == userId) {
            throw new BadRequestException("Admin cannot delete their own account");
        }
        
        // Find the user to delete
        User userToDelete = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        
        // Delete associated entity based on role
        switch (userToDelete.getRole()) {
            case PATIENT:
                patientRepository.findById(userId).ifPresent(patientRepository::delete);
                break;
            case DOCTOR:
                doctorRepository.findById(userId).ifPresent(doctorRepository::delete);
                break;
            case PHARMACY:
                pharmacyRepository.findById(userId).ifPresent(pharmacyRepository::delete);
                break;
            case ADMIN:
                throw new BadRequestException("Cannot delete admin accounts");
            default:
                throw new BadRequestException("Unknown user role: " + userToDelete.getRole());
        }
        
        // Delete the user
        userRepository.delete(userToDelete);
    }

    private UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
