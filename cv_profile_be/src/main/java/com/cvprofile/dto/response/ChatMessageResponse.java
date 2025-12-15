package com.cvprofile.dto.response;

import com.cvprofile.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {
    private Long id;
    private String sender;
    private String content;
    private ChatMessage.MessageType type;
    private LocalDateTime timestamp;
}
