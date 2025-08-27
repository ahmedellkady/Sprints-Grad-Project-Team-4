package com.team4.hospital_system.dto.response;

import com.team4.hospital_system.model.enums.AppointmentStatus;
import java.time.LocalDateTime;

public record AppointmentDto(
        Long id,
        Long doctorId,
        Long patientId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        AppointmentStatus status
) {}
