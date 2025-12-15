package com.cvprofile.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
    
    @NotBlank(message = "Event name is required")
    private String name;
    
    private String organizer;
    private LocalDate eventDate;
    private String location;
    private String role;
    private String description;
}
