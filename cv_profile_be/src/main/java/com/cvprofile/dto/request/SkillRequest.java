package com.cvprofile.dto.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillRequest {
    private String name;
    private String category;
    private Integer proficiencyLevel;
}
