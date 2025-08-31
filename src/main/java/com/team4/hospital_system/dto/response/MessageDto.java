package com.team4.hospital_system.dto.response;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record MessageDto(
    Long id,
    Long senderId,
    String senderName,
    String senderRole,
    Long receiverId,
    String receiverName,
    String receiverRole,
    String content,
    LocalDateTime timestamp,
    boolean isRead
) {}
