package com.cvprofile.service;

import com.cvprofile.dto.request.ChatMessageRequest;
import com.cvprofile.dto.response.ChatMessageResponse;
import com.cvprofile.entity.ChatMessage;
import com.cvprofile.exception.ApiException;
import com.cvprofile.exception.ErrorCode;
import com.cvprofile.mapper.ChatMessageMapper;
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
    private final ChatMessageMapper chatMessageMapper;
    
    @Transactional
    public ChatMessageResponse saveMessage(ChatMessageRequest request) {
        ChatMessage message = chatMessageMapper.toEntity(request);
        ChatMessage saved = chatMessageRepository.save(message);
        return chatMessageMapper.toResponse(saved);
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
                .orElseThrow(() -> new ApiException(ErrorCode.CHAT_MESSAGE_NOT_FOUND, "Message not found with id: " + messageId));
        message.setIsRead(true);
        chatMessageRepository.save(message);
    }
    
    @Transactional
    public void deleteOldMessages(int daysToKeep) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysToKeep);
        chatMessageRepository.deleteByTimestampBefore(cutoffDate);
    }
}
