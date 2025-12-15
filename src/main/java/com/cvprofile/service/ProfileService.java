package com.cvprofile.service;

import com.cvprofile.entity.Profile;
import com.cvprofile.exception.ApiException;
import com.cvprofile.exception.ErrorCode;
import com.cvprofile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    
    private final ProfileRepository profileRepository;
    
    @Transactional(readOnly = true)
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Profile> getProfileByEmail(String email) {
        return profileRepository.findByEmail(email);
    }
    
    @Transactional
    public Profile createProfile(Profile profile) {
        if (profileRepository.existsByEmail(profile.getEmail())) {
            throw new ApiException(ErrorCode.EMAIL_EXISTS, "Email already exists: " + profile.getEmail());
        }
        return profileRepository.save(profile);
    }
    
    @Transactional
    public Profile updateProfile(Long id, Profile profileDetails) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.PROFILE_NOT_FOUND, "Profile not found with id: " + id));
        
        profile.setFullName(profileDetails.getFullName());
        profile.setEmail(profileDetails.getEmail());
        profile.setPhone(profileDetails.getPhone());
        profile.setAddress(profileDetails.getAddress());
        profile.setBirthDate(profileDetails.getBirthDate());
        profile.setSummary(profileDetails.getSummary());
        profile.setAvatar(profileDetails.getAvatar());
        profile.setLinkedin(profileDetails.getLinkedin());
        profile.setGithub(profileDetails.getGithub());
        profile.setWebsite(profileDetails.getWebsite());
        
        return profileRepository.save(profile);
    }
    
    @Transactional
    public void deleteProfile(Long id) {
        if (!profileRepository.existsById(id)) {
            throw new ApiException(ErrorCode.PROFILE_NOT_FOUND, "Profile not found with id: " + id);
        }
        profileRepository.deleteById(id);
    }
}
