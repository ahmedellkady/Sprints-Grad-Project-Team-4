package com.team4.hospital_system.service;

import com.team4.hospital_system.dto.response.AppointmentDto;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    AppointmentDto book(long patientId, long doctorId, LocalDateTime start);
    List<AppointmentDto> listForPatient(long patientId);
    List<AppointmentDto> listForDoctor(long doctorId);
    @Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    // === Use Case 1: Reschedule Appointment ===
    @Transactional
    public AppointmentDto rescheduleAppointment(Long appointmentId, RescheduleAppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        appointment.setAppointmentTime(request.getNewTime());
        Appointment updated = appointmentRepository.save(appointment);

        return AppointmentMapper.toDto(updated);
    }

    // === Use Case 1: Cancel Appointment ===
    @Transactional
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        appointmentRepository.delete(appointment);
    }

    // === Use Case 2: Accept Appointment ===
    @Transactional
    public AppointmentDto acceptAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        appointment.setStatus("ACCEPTED");
        Appointment updated = appointmentRepository.save(appointment);

        return AppointmentMapper.toDto(updated);
        @Service
public class DoctorService {

    private final PatientRepository patientRepository;

    public DoctorService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientHistoryDto> getPatientHistory(Long doctorId, Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        // you can also check doctorId if you want access control

        return patient.getHistory().stream()
                .map(PatientHistoryMapper::toDto)
                .toList();
    }

}

}
