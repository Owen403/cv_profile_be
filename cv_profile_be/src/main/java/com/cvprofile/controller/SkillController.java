package com.cvprofile.controller;

import com.cvprofile.dto.request.SkillRequest;
import com.cvprofile.dto.response.ApiResponse;
import com.cvprofile.dto.response.SkillResponse;
import com.cvprofile.service.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/skills")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SkillController {
    
    private final SkillService skillService;
    
    @GetMapping
    public ApiResponse<List<SkillResponse>> getSkillsByUserId(@PathVariable Long userId) {
        return ApiResponse.<List<SkillResponse>>builder()
                .success(true)
                .message("Get skills successfully")
                .data(skillService.getSkillsByUserId(userId))
                .build();
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SkillResponse> createSkill(@PathVariable Long userId, @Valid @RequestBody SkillRequest request) {
        return ApiResponse.<SkillResponse>builder()
                .success(true)
                .message("Create skill successfully")
                .data(skillService.createSkill(userId, request))
                .build();
    }
    
    @PutMapping("/{skillId}")
    public ApiResponse<SkillResponse> updateSkill(@PathVariable Long skillId, @Valid @RequestBody SkillRequest request) {
        return ApiResponse.<SkillResponse>builder()
                .success(true)
                .message("Update skill successfully")
                .data(skillService.updateSkill(skillId, request))
                .build();
    }
    
    @DeleteMapping("/{skillId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSkill(@PathVariable Long skillId) {
        skillService.deleteSkill(skillId);
    }
}
