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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessagingServiceImplTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MessagingServiceImpl messagingService;

    private User doctor;
    private User patient;
    private Message message;

    @BeforeEach
    void setUp() {
        // Setup test data
        doctor = new User();
        doctor.setId(1L);
        doctor.setName("Dr. Smith");
        doctor.setEmail("doctor@example.com");
        doctor.setHashedPassword("hashedPassword");
        doctor.setRole(Role.DOCTOR);

        patient = new User();
        patient.setId(2L);
        patient.setName("Patient Jones");
        patient.setEmail("patient@example.com");
        patient.setHashedPassword("hashedPassword");
        patient.setRole(Role.PATIENT);

        message = new Message();
        message.setId(1L);
        message.setSender(doctor);
        message.setReceiver(patient);
        message.setContent("Hello, how are you feeling today?");
        message.setTimestamp(LocalDateTime.now());
        message.setRead(false);
    }

    @Test
    void sendMessage_ShouldCreateAndReturnMessage() {
        // Arrange
        when(userRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(userRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        // Act
        MessageDto result = messagingService.send(doctor.getId(), patient.getId(), "Hello, how are you feeling today?");

        // Assert
        assertNotNull(result);
        assertEquals(doctor.getId(), result.senderId());
        assertEquals(patient.getId(), result.receiverId());
        assertEquals("Hello, how are you feeling today?", result.content());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void sendMessage_ShouldThrowWhenSenderNotFound() {
        // Arrange
        when(userRepository.findById(doctor.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            messagingService.send(doctor.getId(), patient.getId(), "Hello"));
    }

    @Test
    void getConversation_ShouldReturnMessagesBetweenUsers() {
        // Arrange
        when(userRepository.existsById(doctor.getId())).thenReturn(true);
        when(userRepository.existsById(patient.getId())).thenReturn(true);
        when(messageRepository.findConversation(doctor.getId(), patient.getId())).thenReturn(List.of(message));

        // Act
        List<MessageDto> result = messagingService.getConversation(doctor.getId(), patient.getId());

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(doctor.getId(), result.get(0).senderId());
        assertEquals(patient.getId(), result.get(0).receiverId());
    }

    @Test
    void markAsRead_ShouldMarkMessageAsRead() {
        // Arrange
        when(messageRepository.findById(message.getId())).thenReturn(Optional.of(message));

        // Act
        messagingService.markAsRead(patient.getId(), message.getId());

        // Assert
        assertTrue(message.isRead());
        verify(messageRepository, times(1)).save(message);
    }

    @Test
    void markAsRead_ShouldThrowWhenUserIsNotReceiver() {
        // Arrange
        when(messageRepository.findById(message.getId())).thenReturn(Optional.of(message));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            messagingService.markAsRead(doctor.getId(), message.getId()));
    }
}
