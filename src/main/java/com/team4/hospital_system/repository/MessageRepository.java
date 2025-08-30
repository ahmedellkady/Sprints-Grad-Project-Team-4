package com.team4.hospital_system.repository;

import com.team4.hospital_system.model.Message;
import com.team4.hospital_system.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    /**
     * Find all messages between two users in order of timestamp
     */
    @Query("SELECT m FROM Message m WHERE " +
           "(m.sender.id = :userAId AND m.receiver.id = :userBId) OR " +
           "(m.sender.id = :userBId AND m.receiver.id = :userAId) " +
           "ORDER BY m.timestamp ASC")
    List<Message> findConversation(@Param("userAId") Long userAId, @Param("userBId") Long userBId);

    /**
     * Find all users who have exchanged messages with a given user
     */
    @Query("SELECT DISTINCT " +
           "CASE WHEN m.sender.id = :userId THEN m.receiver ELSE m.sender END " +
           "FROM Message m " +
           "WHERE m.sender.id = :userId OR m.receiver.id = :userId")
    Set<User> findConversationPartners(@Param("userId") Long userId);

    /**
     * Find the latest message between the user and each of their conversation partners
     */
    @Query(value = "SELECT m.* FROM messages m " +
           "INNER JOIN (" +
           "    SELECT " +
           "        CASE " +
           "            WHEN sender_id = :userId THEN receiver_id " +
           "            ELSE sender_id " +
           "        END AS partner_id, " +
           "        MAX(timestamp) as latest_time " +
           "    FROM messages " +
           "    WHERE sender_id = :userId OR receiver_id = :userId " +
           "    GROUP BY partner_id" +
           ") latest ON " +
           "((m.sender_id = :userId AND m.receiver_id = latest.partner_id) OR " +
           "(m.receiver_id = :userId AND m.sender_id = latest.partner_id)) AND " +
           "m.timestamp = latest.latest_time " +
           "ORDER BY m.timestamp DESC", 
           nativeQuery = true)
    List<Message> findLatestMessagesForEachConversation(@Param("userId") Long userId);
    
    /**
     * Find all unread messages for a user
     */
    List<Message> findByReceiverIdAndIsReadFalse(Long receiverId);
}
