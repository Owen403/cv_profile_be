package com.cvprofile.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicationRequest {
    
    @NotBlank(message = "Publication title is required")
    private String title;
    
    private String authors;
    private String journal;
    private LocalDate publishDate;
    private String doi;
    private String url;
    private Integer citations;
}
