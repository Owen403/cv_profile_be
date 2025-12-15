package com.cvprofile.controller;

import com.cvprofile.dto.request.ProjectRequest;
import com.cvprofile.dto.response.ApiResponse;
import com.cvprofile.dto.response.ProjectResponse;
import com.cvprofile.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/projects")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProjectController {
    
    private final ProjectService projectService;
    
    @GetMapping
    public ApiResponse<List<ProjectResponse>> getProjectsByUserId(@PathVariable Long userId) {
        return ApiResponse.<List<ProjectResponse>>builder()
                .success(true)
                .message("Get projects successfully")
                .data(projectService.getProjectsByUserId(userId))
                .build();
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProjectResponse> createProject(@PathVariable Long userId, @Valid @RequestBody ProjectRequest request) {
        return ApiResponse.<ProjectResponse>builder()
                .success(true)
                .message("Create project successfully")
                .data(projectService.createProject(userId, request))
                .build();
    }
    
    @PutMapping("/{projectId}")
    public ApiResponse<ProjectResponse> updateProject(@PathVariable Long projectId, @Valid @RequestBody ProjectRequest request) {
        return ApiResponse.<ProjectResponse>builder()
                .success(true)
                .message("Update project successfully")
                .data(projectService.updateProject(projectId, request))
                .build();
    }
    
    @DeleteMapping("/{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
    }
}
