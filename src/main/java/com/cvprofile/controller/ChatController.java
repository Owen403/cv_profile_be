package com.cvprofile.controller;

import com.cvprofile.entity.ChatMessage;
import com.cvprofile.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {
    
    private final ChatService chatService;
    
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatService.saveMessage(chatMessage);
    }
    
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, 
                               SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
    
    @GetMapping("/api/chat/history")
    @ResponseBody
    public ResponseEntity<List<ChatMessage>> getChatHistory(
            @RequestParam String sender, 
            @RequestParam String recipient) {
        return ResponseEntity.ok(chatService.getChatHistory(sender, recipient));
    }
    
    @GetMapping("/api/chat/unread/{recipient}")
    @ResponseBody
    public ResponseEntity<List<ChatMessage>> getUnreadMessages(@PathVariable String recipient) {
        return ResponseEntity.ok(chatService.getUnreadMessages(recipient));
    }
    
    @PutMapping("/api/chat/read/{messageId}")
    @ResponseBody
    public ResponseEntity<Void> markAsRead(@PathVariable Long messageId) {
        chatService.markAsRead(messageId);
        return ResponseEntity.ok().build();
    }
}
