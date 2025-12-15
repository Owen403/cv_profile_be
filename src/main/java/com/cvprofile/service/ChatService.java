package com.cvprofile.service;

import com.cvprofile.entity.ChatMessage;
import com.cvprofile.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    
    private final ChatMessageRepository chatMessageRepository;
    
    @Transactional
    public ChatMessage saveMessage(ChatMessage message) {
        message.setTimestamp(LocalDateTime.now());
        return chatMessageRepository.save(message);
    }
    
    @Transactional(readOnly = true)
    public List<ChatMessage> getChatHistory(String sender, String recipient) {
        return chatMessageRepository.findBySenderAndRecipientOrderByTimestampAsc(sender, recipient);
    }
    
    @Transactional(readOnly = true)
    public List<ChatMessage> getUnreadMessages(String recipient) {
        return chatMessageRepository.findByRecipientAndIsReadFalse(recipient);
    }
    
    @Transactional
    public void markAsRead(Long messageId) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + messageId));
        message.setIsRead(true);
        chatMessageRepository.save(message);
    }
    
    @Transactional
    public void deleteOldMessages(int daysToKeep) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysToKeep);
        chatMessageRepository.deleteByTimestampBefore(cutoffDate);
    }
}
