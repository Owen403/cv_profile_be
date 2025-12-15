package com.cvprofile.service;

import com.cvprofile.dto.request.ProjectRequest;
import com.cvprofile.dto.response.ProjectResponse;
import com.cvprofile.entity.Project;
import com.cvprofile.entity.User;
import com.cvprofile.exception.ApiException;
import com.cvprofile.exception.ErrorCode;
import com.cvprofile.mapper.ProjectMapper;
import com.cvprofile.repository.ProjectRepository;
import com.cvprofile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;
    
    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjectsByUserId(Long userId) {
        return projectRepository.findByUserIdOrderByStartDateDesc(userId).stream()
                .map(projectMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public ProjectResponse createProject(Long userId, ProjectRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "User not found with id: " + userId));
        
        Project project = projectMapper.toEntity(request, user);
        Project saved = projectRepository.save(project);
        return projectMapper.toResponse(saved);
    }
    
    @Transactional
    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.PROJECT_NOT_FOUND, "Project not found with id: " + id));
        
        projectMapper.updateEntityFromRequest(request, project);
        Project updated = projectRepository.save(project);
        return projectMapper.toResponse(updated);
    }
    
    @Transactional
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ApiException(ErrorCode.PROJECT_NOT_FOUND, "Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }
}
