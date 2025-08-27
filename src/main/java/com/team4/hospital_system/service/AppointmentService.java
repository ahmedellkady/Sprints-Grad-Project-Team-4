package com.team4.hospital_system.service;

import com.team4.hospital_system.dto.response.AppointmentDto;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    AppointmentDto book(long patientId, long doctorId, LocalDateTime start);
    List<AppointmentDto> listForPatient(long patientId);
    List<AppointmentDto> listForDoctor(long doctorId);

    AppointmentDto accept(long doctorId, long appointmentId);
    AppointmentDto reschedule(long userId, long appointmentId, LocalDateTime newStart); //both patient and doctor can reschedule
    void cancel(long userId, long appointmentId, String note); //both patient and doctor can cancel

    // Follow-up (links new appointment to previous via prevAppointment)
    AppointmentDto recordFollowUp(long doctorId, long previousAppointmentId, LocalDateTime followUpStart, String note);
}
