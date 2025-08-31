package com.team4.hospital_system.dto.response;

import java.time.LocalDateTime;

public record FollowUpResponseDto(
        Long id,
        Long doctorId,
        String doctorName,
        Long patientId,
        String patientName,
        String notes,
        String patientStatus,
        LocalDateTime followUpDate,
        LocalDateTime createdAt
) {}
