package com.team4.hospital_system.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AppointmentRequestDto(
        @NotNull Long doctorId,
        @NotNull @Future LocalDateTime appointmentTime
) {}
