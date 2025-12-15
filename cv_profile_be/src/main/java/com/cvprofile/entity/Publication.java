package com.cvprofile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;

@Entity
@Table(name = "publications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 5000)
    private String abstractText;
    
    private String authors;
    
    private String journal;
    
    private String conference;
    
    private LocalDate publishedDate;
    
    private String doi;
    
    private String url;
    
    private String citation;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
}
