package com.team4.hospital_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SendMessageDto(
    @NotNull(message = "Recipient ID cannot be null")
    Long recipientId,
    
    @NotBlank(message = "Message content cannot be empty")
    @Size(min = 1, max = 1000, message = "Message content must be between 1 and 1000 characters")
    String content
) {}
