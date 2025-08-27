package com.team4.hospital_system.service;

import com.team4.hospital_system.dto.response.ConversationSummaryDto;
import com.team4.hospital_system.dto.response.MessageDto;

import java.util.List;

public interface MessagingService {
    MessageDto send(long senderUserId, long receiverUserId, String body);
    List<MessageDto> getConversation(long userAId, long userBId);
    List<ConversationSummaryDto> listConversations(long userId);
    void markAsRead(long userId, long messageId);
}
