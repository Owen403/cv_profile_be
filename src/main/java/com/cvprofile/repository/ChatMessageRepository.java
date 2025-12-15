package com.cvprofile.repository;

import com.cvprofile.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderAndRecipientOrderByTimestampAsc(String sender, String recipient);
    List<ChatMessage> findByRecipientAndIsReadFalse(String recipient);
    List<ChatMessage> findByTimestampAfter(LocalDateTime timestamp);
    void deleteByTimestampBefore(LocalDateTime timestamp);
}
