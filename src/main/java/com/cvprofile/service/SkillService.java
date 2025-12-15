package com.cvprofile.service;

import com.cvprofile.entity.Skill;
import com.cvprofile.entity.Profile;
import com.cvprofile.exception.ApiException;
import com.cvprofile.exception.ErrorCode;
import com.cvprofile.repository.SkillRepository;
import com.cvprofile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillService {
    
    private final SkillRepository skillRepository;
    private final ProfileRepository profileRepository;
    
    @Transactional(readOnly = true)
    public List<Skill> getSkillsByProfileId(Long profileId) {
        return skillRepository.findByProfileId(profileId);
    }
    
    @Transactional(readOnly = true)
    public List<Skill> getSkillsByProfileIdAndCategory(Long profileId, String category) {
        return skillRepository.findByProfileIdAndCategory(profileId, category);
    }
    
    @Transactional
    public Skill createSkill(Long profileId, Skill skill) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ApiException(ErrorCode.PROFILE_NOT_FOUND, "Profile not found with id: " + profileId));
        
        skill.setProfile(profile);
        return skillRepository.save(skill);
    }
    
    @Transactional
    public Skill updateSkill(Long id, Skill skillDetails) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.SKILL_NOT_FOUND, "Skill not found with id: " + id));
        
        skill.setName(skillDetails.getName());
        skill.setCategory(skillDetails.getCategory());
        skill.setLevel(skillDetails.getLevel());
        skill.setDescription(skillDetails.getDescription());
        
        return skillRepository.save(skill);
    }
    
    @Transactional
    public void deleteSkill(Long id) {
        if (!skillRepository.existsById(id)) {
            throw new ApiException(ErrorCode.SKILL_NOT_FOUND, "Skill not found with id: " + id);
        }
        skillRepository.deleteById(id);
    }
}
