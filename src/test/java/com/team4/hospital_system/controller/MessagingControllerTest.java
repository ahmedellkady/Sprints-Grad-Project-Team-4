package com.team4.hospital_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.hospital_system.dto.request.SendMessageDto;
import com.team4.hospital_system.dto.response.ConversationSummaryDto;
import com.team4.hospital_system.dto.response.MessageDto;
import com.team4.hospital_system.model.User;
import com.team4.hospital_system.model.UserPrinciple;
import com.team4.hospital_system.model.enums.Role;
import com.team4.hospital_system.service.MessagingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessagingController.class)
@AutoConfigureMockMvc
public class MessagingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MessagingService messagingService;

    private MessageDto messageDto;
    private UserPrinciple userPrinciple;
    private User doctor;
    
    @BeforeEach
    void setUp() {
        doctor = new User();
        doctor.setId(1L);
        doctor.setName("Dr. Smith");
        doctor.setEmail("doctor@example.com");
        doctor.setRole(Role.DOCTOR);
        userPrinciple = new UserPrinciple(Optional.of(doctor));
        
        LocalDateTime now = LocalDateTime.now();
        messageDto = MessageDto.builder()
                .id(1L)
                .senderId(1L)
                .senderName("Dr. Smith")
                .senderRole("DOCTOR")
                .receiverId(2L)
                .receiverName("Patient Jones")
                .receiverRole("PATIENT")
                .content("Hello, how are you feeling today?")
                .timestamp(now)
                .isRead(false)
                .build();
    }

    @Test
    @WithMockUser(authorities = "DOCTOR")
    void sendMessage_ShouldReturnCreatedMessage() throws Exception {
        // Arrange
        SendMessageDto request = new SendMessageDto(2L, "Hello, how are you feeling today?");
        given(messagingService.send(anyLong(), eq(2L), eq("Hello, how are you feeling today?")))
                .willReturn(messageDto);

        // Act & Assert
        mockMvc.perform(post("/api/messages/send")
                .with(user(userPrinciple))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(messageDto.id()))
                .andExpect(jsonPath("$.content").value(messageDto.content()));
    }

    @Test
    @WithMockUser(authorities = "DOCTOR")
    void listConversations_ShouldReturnConversationSummaries() throws Exception {
        // Arrange
        List<ConversationSummaryDto> summaries = Arrays.asList(
            ConversationSummaryDto.builder()
                .partnerId(2L)
                .partnerName("Patient Jones")
                .partnerRole("PATIENT")
                .lastMessagePreview("Hello, how are you feeling today?")
                .lastMessageTime(LocalDateTime.now())
                .hasUnreadMessages(false)
                .unreadCount(0)
                .build()
        );
        
        given(messagingService.listConversations(anyLong()))
                .willReturn(summaries);

        // Act & Assert
        mockMvc.perform(get("/api/messages/conversations")
                .with(user(userPrinciple)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].partnerId").value(2L))
                .andExpect(jsonPath("$[0].partnerName").value("Patient Jones"));
    }

    @Test
    @WithMockUser(authorities = "DOCTOR")
    void getConversation_ShouldReturnMessages() throws Exception {
        // Arrange
        List<MessageDto> messages = Arrays.asList(messageDto);
        given(messagingService.getConversation(anyLong(), eq(2L)))
                .willReturn(messages);

        // Act & Assert
        mockMvc.perform(get("/api/messages/conversations/2")
                .with(user(userPrinciple)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(messageDto.id()))
                .andExpect(jsonPath("$[0].content").value(messageDto.content()));
    }

    @Test
    @WithMockUser(authorities = "DOCTOR")
    void markAsRead_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(messagingService).markAsRead(anyLong(), eq(1L));

        // Act & Assert
        mockMvc.perform(patch("/api/messages/1/read")
                .with(user(userPrinciple)))
                .andExpect(status().isNoContent());
    }
}
