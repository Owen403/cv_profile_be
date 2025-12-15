package com.cvprofile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String sender;
    
    @Column(nullable = false)
    private String recipient;
    
    @Column(length = 5000, nullable = false)
    private String content;
    
    @Enumerated(EnumType.STRING)
    private MessageType type;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    private Boolean isRead = false;
    
    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
    
    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
