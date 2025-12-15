package com.cvprofile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    private Long id;
    private Long userId;
    private String name;
    private String organizer;
    private LocalDate eventDate;
    private String location;
    private String role;
    private String description;
}
