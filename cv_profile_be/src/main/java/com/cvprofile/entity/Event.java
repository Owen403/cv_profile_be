package com.cvprofile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 5000)
    private String description;
    
    private String eventType; // "Conference", "Workshop", "Seminar", "Competition", etc.
    
    private String location;
    
    private LocalDateTime startDate;
    
    private LocalDateTime endDate;
    
    private String role; // "Participant", "Speaker", "Organizer", etc.
    
    private String certificate;
    
    private String url;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
}
