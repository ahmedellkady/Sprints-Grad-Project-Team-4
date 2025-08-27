package com.team4.hospital_system.service.Impl;

import com.team4.hospital_system.dto.request.AppointmentRequestDto;
import com.team4.hospital_system.dto.response.AppointmentResponseDto;
import com.team4.hospital_system.exception.BadRequestException;
import com.team4.hospital_system.exception.ResourceNotFoundException;
import com.team4.hospital_system.model.Appointment;
import com.team4.hospital_system.model.User;
import com.team4.hospital_system.model.enums.AppointmentStatus;
import com.team4.hospital_system.model.enums.Role;
import com.team4.hospital_system.repository.AppointmentRepository;
import com.team4.hospital_system.repository.UserRepository;
import com.team4.hospital_system.service.AppointmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    private AppointmentResponseDto map(Appointment a) {
        return new AppointmentResponseDto(
                a.getId(),
                a.getDoctor().getId(),
                a.getPatient().getId(),
                a.getAppointmentTime(),
                a.getStatus()
        );
    }

    @Override
    @Transactional
    public AppointmentResponseDto bookAppointment(Long patientId, AppointmentRequestDto request) {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + patientId));
        if (patient.getRole() != Role.PATIENT) throw new BadRequestException("User is not a patient");

        User doctor = userRepository.findById(request.doctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + request.doctorId()));
        if (doctor.getRole() != Role.DOCTOR) throw new BadRequestException("User is not a doctor");

        LocalDateTime slot = request.appointmentTime().withSecond(0).withNano(0);

        if (appointmentRepository.existsByDoctorIdAndAppointmentTime(doctor.getId(), slot)) {
            throw new BadRequestException("Time slot already booked");
        }

        Appointment saved = appointmentRepository.save(Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .appointmentTime(slot)
                .status(AppointmentStatus.BOOKED)
                .build());

        return map(saved);
    }

    @Override
    public List<AppointmentResponseDto> getAppointmentsForPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId)
                .stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponseDto> getDoctorSchedule(Long doctorId, LocalDate day) {
        LocalDateTime start = day.atStartOfDay();
        LocalDateTime end = day.plusDays(1).atStartOfDay().minusSeconds(1);
        return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end)
                .stream().map(this::map).collect(Collectors.toList());
    }
}
