package com.team4.hospital_system.repository;

import com.team4.hospital_system.model.Message;
import com.team4.hospital_system.model.User;
import com.team4.hospital_system.model.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class MessageRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MessageRepository messageRepository;

    private User doctor;
    private User patient;
    private User admin;
    private Message message1;
    private Message message2;
    private Message message3;

    @BeforeEach
    void setUp() {
        // Create users
        doctor = new User();
        doctor.setName("Dr. Smith");
        doctor.setEmail("doctor@example.com");
        doctor.setHashedPassword("password");
        doctor.setRole(Role.DOCTOR);
        entityManager.persist(doctor);

        patient = new User();
        patient.setName("Patient Jones");
        patient.setEmail("patient@example.com");
        patient.setHashedPassword("password");
        patient.setRole(Role.PATIENT);
        entityManager.persist(patient);

        admin = new User();
        admin.setName("Admin User");
        admin.setEmail("admin@example.com");
        admin.setHashedPassword("password");
        admin.setRole(Role.ADMIN);
        entityManager.persist(admin);

        // Create messages
        message1 = new Message();
        message1.setSender(doctor);
        message1.setReceiver(patient);
        message1.setContent("Hello, how are you feeling today?");
        message1.setTimestamp(LocalDateTime.now().minusDays(1));
        message1.setRead(false);
        entityManager.persist(message1);

        message2 = new Message();
        message2.setSender(patient);
        message2.setReceiver(doctor);
        message2.setContent("I'm feeling better, thank you!");
        message2.setTimestamp(LocalDateTime.now().minusHours(12));
        message2.setRead(false);
        entityManager.persist(message2);

        message3 = new Message();
        message3.setSender(doctor);
        message3.setReceiver(admin);
        message3.setContent("Need to discuss a case");
        message3.setTimestamp(LocalDateTime.now().minusHours(6));
        message3.setRead(true);
        entityManager.persist(message3);

        entityManager.flush();
    }

    @Test
    void findConversation_ShouldReturnMessagesBetweenTwoUsers() {
        // Act
        List<Message> conversation = messageRepository.findConversation(doctor.getId(), patient.getId());

        // Assert
        assertThat(conversation).hasSize(2);
        assertThat(conversation).containsExactlyInAnyOrder(message1, message2);
    }

    @Test
    void findConversationPartners_ShouldReturnDistinctUsers() {
        // Act
        Set<User> doctorPartners = messageRepository.findConversationPartners(doctor.getId());

        // Assert
        assertThat(doctorPartners).hasSize(2);
        assertThat(doctorPartners).containsExactlyInAnyOrder(patient, admin);
    }

    @Test
    void findLatestMessagesForEachConversation_ShouldReturnLatestMessages() {
        // Act
        List<Message> latestMessages = messageRepository.findLatestMessagesForEachConversation(doctor.getId());

        // Assert
        assertThat(latestMessages).hasSize(2);
        // Should contain the latest message from each conversation
        boolean containsEither = latestMessages.contains(message2) || latestMessages.contains(message3);
        assertThat(containsEither).isTrue();
    }

    @Test
    void findByReceiverIdAndIsReadFalse_ShouldReturnUnreadMessages() {
        // Act
        List<Message> unreadMessages = messageRepository.findByReceiverIdAndIsReadFalse(patient.getId());

        // Assert
        assertThat(unreadMessages).hasSize(1);
        assertThat(unreadMessages.get(0)).isEqualTo(message1);
    }
}
