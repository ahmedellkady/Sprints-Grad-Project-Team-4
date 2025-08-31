package com.team4.hospital_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record FollowUpRequestDto(
        @NotNull Long patientId,
        @NotBlank String notes,
        @NotBlank String patientStatus,
        @NotNull LocalDateTime followUpDate
) {}
