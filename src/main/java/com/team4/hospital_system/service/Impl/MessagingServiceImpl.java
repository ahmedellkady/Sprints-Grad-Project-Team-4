package com.team4.hospital_system.service.Impl;

import com.team4.hospital_system.dto.response.ConversationSummaryDto;
import com.team4.hospital_system.dto.response.MessageDto;
import com.team4.hospital_system.exception.ResourceNotFoundException;
import com.team4.hospital_system.model.Message;
import com.team4.hospital_system.model.User;
import com.team4.hospital_system.repository.MessageRepository;
import com.team4.hospital_system.repository.UserRepository;
import com.team4.hospital_system.service.MessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessagingServiceImpl implements MessagingService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public MessageDto send(long senderUserId, long receiverUserId, String content) {
        // Fetch sender and receiver
        User sender = userRepository.findById(senderUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found with ID: " + senderUserId));
        
        User receiver = userRepository.findById(receiverUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found with ID: " + receiverUserId));
        
        // Create and save the message
        Message message = new Message();
        message.setContent(content);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setRead(false);
        
        messageRepository.save(message);
        
        // Convert to DTO and return
        return convertToDto(message);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageDto> getConversation(long userAId, long userBId) {
        // Ensure both users exist
        if (!userRepository.existsById(userAId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userAId);
        }
        
        if (!userRepository.existsById(userBId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userBId);
        }
        
        // Get all messages between the two users
        List<Message> messages = messageRepository.findConversation(userAId, userBId);
        
        // Convert to DTOs and return
        return messages.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConversationSummaryDto> listConversations(long userId) {
        // Ensure user exists
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        
        // Get the latest message for each conversation
        List<Message> latestMessages = messageRepository.findLatestMessagesForEachConversation(userId);
        
        // Get unread messages for counting
        List<Message> unreadMessages = messageRepository.findByReceiverIdAndIsReadFalse(userId);
        
        // Group unread messages by sender
        var unreadCountsBySender = unreadMessages.stream()
                .collect(Collectors.groupingBy(
                    message -> message.getSender().getId(),
                    Collectors.counting()
                ));
        
        // Convert to DTOs
        List<ConversationSummaryDto> conversationSummaries = new ArrayList<>();
        
        for (Message message : latestMessages) {
            // Determine the conversation partner (the other user)
            User partner = (message.getSender().getId() == userId) 
                ? message.getReceiver() 
                : message.getSender();
                
            // Count unread messages from this partner
            long unreadCount = unreadCountsBySender.getOrDefault(partner.getId(), 0L);
            
            // Create a preview of the last message (first 50 chars)
            String preview = message.getContent();
            if (preview.length() > 50) {
                preview = preview.substring(0, 47) + "...";
            }
            
            ConversationSummaryDto summary = ConversationSummaryDto.builder()
                .partnerId(partner.getId())
                .partnerName(partner.getName())
                .partnerRole(partner.getRole().toString())
                .lastMessagePreview(preview)
                .lastMessageTime(message.getTimestamp())
                .hasUnreadMessages(unreadCount > 0)
                .unreadCount((int) unreadCount)
                .build();
                
            conversationSummaries.add(summary);
        }
        
        return conversationSummaries;
    }

    @Override
    @Transactional
    public void markAsRead(long userId, long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with ID: " + messageId));
        
        // Check if the user is the receiver of the message
        if (!message.getReceiver().getId().equals(userId)) {
            throw new IllegalArgumentException("User is not authorized to mark this message as read");
        }
        
        // Mark as read if not already
        if (!message.isRead()) {
            message.setRead(true);
            messageRepository.save(message);
        }
    }
    
    /**
     * Helper method to convert Message entity to MessageDto
     */
    private MessageDto convertToDto(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .senderId(message.getSender().getId())
                .senderName(message.getSender().getName())
                .senderRole(message.getSender().getRole().toString())
                .receiverId(message.getReceiver().getId())
                .receiverName(message.getReceiver().getName())
                .receiverRole(message.getReceiver().getRole().toString())
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .isRead(message.isRead())
                .build();
    }
}
