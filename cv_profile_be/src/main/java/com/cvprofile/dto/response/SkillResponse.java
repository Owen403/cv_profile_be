package com.cvprofile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillResponse {
    private Long id;
    private Long userId;
    private String name;
    private String category;
    private Integer proficiencyLevel;
}
