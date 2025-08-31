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
}

}
