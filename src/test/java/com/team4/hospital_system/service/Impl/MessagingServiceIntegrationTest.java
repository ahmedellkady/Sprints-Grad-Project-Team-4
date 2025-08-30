package com.team4.hospital_system.service.Impl;

import com.team4.hospital_system.dto.response.ConversationSummaryDto;
import com.team4.hospital_system.dto.response.MessageDto;
import com.team4.hospital_system.exception.ResourceNotFoundException;
import com.team4.hospital_system.model.Message;
import com.team4.hospital_system.model.User;
import com.team4.hospital_system.model.enums.Role;
import com.team4.hospital_system.repository.MessageRepository;
import com.team4.hospital_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MessagingServiceIntegrationTest {

    @Autowired
    private MessagingServiceImpl messagingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    private User doctor;
    private User patient;

    @BeforeEach
    void setUp() {
        // Create test users
        doctor = new User();
        doctor.setName("Dr. Smith");
        doctor.setEmail("doctor@example.com");
        doctor.setHashedPassword("password");
        doctor.setRole(Role.DOCTOR);
        userRepository.save(doctor);

        patient = new User();
        patient.setName("Patient Jones");
        patient.setEmail("patient@example.com");
        patient.setHashedPassword("password");
        patient.setRole(Role.PATIENT);
        userRepository.save(patient);
    }

    @Test
    void sendMessage_ShouldCreateMessageInDatabase() {
        // Act
        String content = "Hello, how are you feeling today?";
        MessageDto messageDto = messagingService.send(doctor.getId(), patient.getId(), content);

        // Assert
        assertNotNull(messageDto);
        assertEquals(doctor.getId(), messageDto.senderId());
        assertEquals(patient.getId(), messageDto.receiverId());
        assertEquals(content, messageDto.content());

        // Check if message is in database
        List<Message> messages = messageRepository.findConversation(doctor.getId(), patient.getId());
        assertThat(messages).hasSize(1);
        assertEquals(content, messages.get(0).getContent());
    }

    @Test
    void getConversation_ShouldReturnMessagesInOrder() {
        // Arrange
        messagingService.send(doctor.getId(), patient.getId(), "Hello, how are you feeling today?");
        messagingService.send(patient.getId(), doctor.getId(), "I'm feeling better, thank you!");

        // Act
        List<MessageDto> conversation = messagingService.getConversation(doctor.getId(), patient.getId());

        // Assert
        assertThat(conversation).hasSize(2);
        assertEquals("Hello, how are you feeling today?", conversation.get(0).content());
        assertEquals("I'm feeling better, thank you!", conversation.get(1).content());
    }

    @Test
    void listConversations_ShouldShowSummary() {
        // Arrange
        messagingService.send(doctor.getId(), patient.getId(), "Hello, how are you feeling today?");

        // Act
        List<ConversationSummaryDto> doctorConversations = messagingService.listConversations(doctor.getId());
        List<ConversationSummaryDto> patientConversations = messagingService.listConversations(patient.getId());

        // Assert
        assertThat(doctorConversations).hasSize(1);
        assertThat(patientConversations).hasSize(1);
        
        assertEquals(patient.getId(), doctorConversations.get(0).partnerId());
        assertEquals(doctor.getId(), patientConversations.get(0).partnerId());
        
        assertEquals("Hello, how are you feeling today?", doctorConversations.get(0).lastMessagePreview());
        assertEquals("Hello, how are you feeling today?", patientConversations.get(0).lastMessagePreview());
        
        assertFalse(doctorConversations.get(0).hasUnreadMessages());
        assertTrue(patientConversations.get(0).hasUnreadMessages());
    }

    @Test
    void markAsRead_ShouldUpdateMessageReadStatus() {
        // Arrange
        MessageDto sentMessage = messagingService.send(doctor.getId(), patient.getId(), "Hello, how are you feeling today?");
        
        // Initial state check
        Message message = messageRepository.findById(sentMessage.id()).orElseThrow();
        assertFalse(message.isRead());
        
        // Act
        messagingService.markAsRead(patient.getId(), sentMessage.id());
        
        // Assert
        message = messageRepository.findById(sentMessage.id()).orElseThrow();
        assertTrue(message.isRead());
    }

    @Test
    void markAsRead_ShouldThrowException_WhenUserIsNotReceiver() {
        // Arrange
        MessageDto sentMessage = messagingService.send(doctor.getId(), patient.getId(), "Hello, how are you feeling today?");
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            messagingService.markAsRead(doctor.getId(), sentMessage.id())
        );
    }

    @Test
    void send_ShouldThrowException_WhenReceiverNotFound() {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            messagingService.send(doctor.getId(), 99999L, "Hello")
        );
    }
}
