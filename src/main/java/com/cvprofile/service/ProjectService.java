package com.cvprofile.service;

import com.cvprofile.entity.Project;
import com.cvprofile.entity.Profile;
import com.cvprofile.exception.ApiException;
import com.cvprofile.exception.ErrorCode;
import com.cvprofile.repository.ProjectRepository;
import com.cvprofile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    
    private final ProjectRepository projectRepository;
    private final ProfileRepository profileRepository;
    
    @Transactional(readOnly = true)
    public List<Project> getProjectsByProfileId(Long profileId) {
        return projectRepository.findByProfileIdOrderByStartDateDesc(profileId);
    }
    
    @Transactional
    public Project createProject(Long profileId, Project project) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ApiException(ErrorCode.PROFILE_NOT_FOUND, "Profile not found with id: " + profileId));
        
        project.setProfile(profile);
        return projectRepository.save(project);
    }
    
    @Transactional
    public Project updateProject(Long id, Project projectDetails) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.PROJECT_NOT_FOUND, "Project not found with id: " + id));
        
        project.setTitle(projectDetails.getTitle());
        project.setDescription(projectDetails.getDescription());
        project.setTechnologies(projectDetails.getTechnologies());
        project.setRole(projectDetails.getRole());
        project.setStartDate(projectDetails.getStartDate());
        project.setEndDate(projectDetails.getEndDate());
        project.setProjectUrl(projectDetails.getProjectUrl());
        project.setGithubUrl(projectDetails.getGithubUrl());
        project.setAchievements(projectDetails.getAchievements());
        
        return projectRepository.save(project);
    @Transactional
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ApiException(ErrorCode.PROJECT_NOT_FOUND, "Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }
}       projectRepository.deleteById(id);
    }
}
