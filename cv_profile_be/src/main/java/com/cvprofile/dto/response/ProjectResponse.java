package com.cvprofile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String technologies;
    private String role;
    private LocalDate startDate;
    private LocalDate endDate;
    private String projectUrl;
    private String githubUrl;
    private String achievements;
}
