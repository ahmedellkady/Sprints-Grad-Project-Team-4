package com.team4.hospital_system.dto.response;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record ConversationSummaryDto(
    Long partnerId,
    String partnerName,
    String partnerRole,
    String lastMessagePreview,
    LocalDateTime lastMessageTime,
    boolean hasUnreadMessages,
    int unreadCount
) {}
