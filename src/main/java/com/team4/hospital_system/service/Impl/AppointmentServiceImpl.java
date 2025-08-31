package com.team4.hospital_system.service.Impl;

import com.team4.hospital_system.dto.response.AppointmentDto;
import com.team4.hospital_system.dto.request.RescheduleAppointmentRequest;
import com.team4.hospital_system.exception.BadRequestException;
import com.team4.hospital_system.exception.ResourceNotFoundException;
import com.team4.hospital_system.model.Appointment;
import com.team4.hospital_system.model.Doctor;
import com.team4.hospital_system.model.Patient;
import com.team4.hospital_system.model.enums.AppointmentStatus;
import com.team4.hospital_system.repository.AppointmentRepository;
import com.team4.hospital_system.repository.DoctorRepository;
import com.team4.hospital_system.repository.PatientRepository;
import com.team4.hospital_system.service.AppointmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  DoctorRepository doctorRepository,
                                  PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    private static final Duration DEFAULT_DURATION = Duration.ofMinutes(30);

    @Override
    public AppointmentDto book(long patientId, long doctorId, LocalDateTime start) {
        if (start.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Appointment time must be in the future");
        }
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + doctorId));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + patientId));

        LocalDateTime end = start.plus(DEFAULT_DURATION);
        boolean overlapping = !appointmentRepository.findDoctorOverlaps(doctorId, start, end).isEmpty();
        if (overlapping) {
            throw new BadRequestException("Requested time overlaps with an existing appointment");
        }

        Appointment appt = Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .startTime(start)
                .endTime(end)
                .status(AppointmentStatus.BOOKED)
                .build();
        appt = appointmentRepository.save(appt);
        return toDto(appt);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDto> listForPatient(long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(AppointmentServiceImpl::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDto> listForDoctor(long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .filter(a -> a.getDoctor().getId().equals(doctorId))
                .map(AppointmentServiceImpl::toDto)
                .toList();
    }

    // === Use Case 1: Reschedule Appointment ===
    @Override
    public AppointmentDto rescheduleAppointment(Long appointmentId, RescheduleAppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        appointment.setStartTime(request.getNewTime());
        appointment.setEndTime(request.getNewTime().plus(DEFAULT_DURATION));
        Appointment updated = appointmentRepository.save(appointment);

        return toDto(updated);
    }

    // === Use Case 1: Cancel Appointment ===
    @Override
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        appointmentRepository.delete(appointment);
    }

    // === Use Case 2: Accept Appointment ===
    @Override
    public AppointmentDto acceptAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.ACCEPTED);
        Appointment updated = appointmentRepository.save(appointment);

        return toDto(updated);
    }

    private static AppointmentDto toDto(Appointment a) {
        return new AppointmentDto(
                a.getId(),
                a.getDoctor().getId(),
                a.getPatient().getId(),
                a.getStartTime(),
                a.getEndTime(),
                a.getStatus()
        );
    }
}

