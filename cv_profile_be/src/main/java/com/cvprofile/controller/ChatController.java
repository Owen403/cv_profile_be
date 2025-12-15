package com.cvprofile.controller;

import com.cvprofile.dto.request.ChatMessageRequest;
import com.cvprofile.dto.response.ChatMessageResponse;
import com.cvprofile.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    
    private final ChatService chatService;
    
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessageResponse sendMessage(@Payload ChatMessageRequest request) {
        return chatService.saveMessage(request);
    }
    
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessageRequest addUser(@Payload ChatMessageRequest request, 
                               SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", request.getSender());
        return request;
    }
}
