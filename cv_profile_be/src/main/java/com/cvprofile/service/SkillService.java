package com.cvprofile.service;

import com.cvprofile.dto.request.SkillRequest;
import com.cvprofile.dto.response.SkillResponse;
import com.cvprofile.entity.Skill;
import com.cvprofile.entity.User;
import com.cvprofile.exception.ApiException;
import com.cvprofile.exception.ErrorCode;
import com.cvprofile.mapper.SkillMapper;
import com.cvprofile.repository.SkillRepository;
import com.cvprofile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillService {
    
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    private final SkillMapper skillMapper;
    
    @Transactional(readOnly = true)
    public List<SkillResponse> getSkillsByUserId(Long userId) {
        return skillRepository.findByUserId(userId).stream()
                .map(skillMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public SkillResponse createSkill(Long userId, SkillRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "User not found with id: " + userId));
        
        // Validate proficiency level
        if (request.getProficiencyLevel() != null && 
            (request.getProficiencyLevel() < 1 || request.getProficiencyLevel() > 5)) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Proficiency level must be between 1 and 5");
        }
        
        Skill skill = skillMapper.toEntity(request, user);
        Skill saved = skillRepository.save(skill);
        return skillMapper.toResponse(saved);
    }
    
    @Transactional
    public SkillResponse updateSkill(Long id, SkillRequest request) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.SKILL_NOT_FOUND, "Skill not found with id: " + id));
        
        // Validate proficiency level
        if (request.getProficiencyLevel() != null && 
            (request.getProficiencyLevel() < 1 || request.getProficiencyLevel() > 5)) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Proficiency level must be between 1 and 5");
        }
        
        skillMapper.updateEntityFromRequest(request, skill);
        Skill updated = skillRepository.save(skill);
        return skillMapper.toResponse(updated);
    }
    
    @Transactional
    public void deleteSkill(Long id) {
        if (!skillRepository.existsById(id)) {
            throw new ApiException(ErrorCode.SKILL_NOT_FOUND, "Skill not found with id: " + id);
        }
        skillRepository.deleteById(id);
    }
}
