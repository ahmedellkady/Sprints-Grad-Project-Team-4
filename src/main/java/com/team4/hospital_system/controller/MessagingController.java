package com.team4.hospital_system.controller;

import com.team4.hospital_system.dto.request.SendMessageDto;
import com.team4.hospital_system.dto.response.ConversationSummaryDto;
import com.team4.hospital_system.dto.response.MessageDto;
import com.team4.hospital_system.model.UserPrinciple;
import com.team4.hospital_system.service.MessagingService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessagingController {

    private final MessagingService messagingService;

    public MessagingController(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    /**
     * Send a message to another user
     */
    @PostMapping("/send")
    @PreAuthorize("hasAnyAuthority('DOCTOR', 'PATIENT')")
    public ResponseEntity<MessageDto> sendMessage(
            @AuthenticationPrincipal UserPrinciple userPrinciple,
            @Valid @RequestBody SendMessageDto request) {
        
        long senderId = userPrinciple.getUser()
                .orElseThrow(() -> new IllegalStateException("User not found in authentication"))
                .getId();
                
        MessageDto message = messagingService.send(senderId, request.recipientId(), request.content());
        
        return ResponseEntity
                .created(URI.create("/api/messages/" + message.id()))
                .body(message);
    }

    /**
     * List all conversations for the authenticated user
     */
    @GetMapping("/conversations")
    @PreAuthorize("hasAnyAuthority('DOCTOR', 'PATIENT')")
    public ResponseEntity<List<ConversationSummaryDto>> listConversations(
            @AuthenticationPrincipal UserPrinciple userPrinciple) {
        
        long userId = userPrinciple.getUser()
                .orElseThrow(() -> new IllegalStateException("User not found in authentication"))
                .getId();
                
        return ResponseEntity.ok(messagingService.listConversations(userId));
    }

    /**
     * Get all messages in a conversation between the authenticated user and another user
     */
    @GetMapping("/conversations/{partnerId}")
    @PreAuthorize("hasAnyAuthority('DOCTOR', 'PATIENT')")
    public ResponseEntity<List<MessageDto>> getConversation(
            @AuthenticationPrincipal UserPrinciple userPrinciple,
            @PathVariable long partnerId) {
        
        long userId = userPrinciple.getUser()
                .orElseThrow(() -> new IllegalStateException("User not found in authentication"))
                .getId();
                
        return ResponseEntity.ok(messagingService.getConversation(userId, partnerId));
    }

    /**
     * Mark a message as read
     */
    @PatchMapping("/{messageId}/read")
    @PreAuthorize("hasAnyAuthority('DOCTOR', 'PATIENT')")
    public ResponseEntity<Void> markAsRead(
            @AuthenticationPrincipal UserPrinciple userPrinciple,
            @PathVariable long messageId) {
        
        long userId = userPrinciple.getUser()
                .orElseThrow(() -> new IllegalStateException("User not found in authentication"))
                .getId();
                
        messagingService.markAsRead(userId, messageId);
        
        return ResponseEntity.noContent().build();
    }
}
