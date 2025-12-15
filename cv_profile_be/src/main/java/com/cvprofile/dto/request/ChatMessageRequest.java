package com.cvprofile.dto.request;

import com.cvprofile.entity.ChatMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequest {
    
    @NotBlank(message = "Sender is required")
    private String sender;
    
    @NotBlank(message = "Content is required")
    private String content;
    
    private ChatMessage.MessageType type;
}
