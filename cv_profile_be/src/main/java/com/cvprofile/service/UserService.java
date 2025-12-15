package com.cvprofile.service;

import com.cvprofile.dto.request.UpdateProfileRequest;
import com.cvprofile.entity.User;
import com.cvprofile.exception.ApiException;
import com.cvprofile.exception.ErrorCode;
import com.cvprofile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final AuthService authService;
    
    @Transactional
    public User updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "User not found with id: " + userId));
        
        // Verify that the current user is updating their own profile
        User currentUser = authService.getCurrentUser();
        if (!currentUser.getId().equals(userId)) {
            throw new ApiException(ErrorCode.FORBIDDEN, "You can only update your own profile");
        }
        
        // Update fields if provided
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
        if (request.getSummary() != null) {
            user.setSummary(request.getSummary());
        }
        if (request.getOrganization() != null) {
            user.setOrganization(request.getOrganization());
        }
        if (request.getHobbies() != null) {
            user.setHobbies(request.getHobbies());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }
        if (request.getLinkedin() != null) {
            user.setLinkedin(request.getLinkedin());
        }
        if (request.getGithub() != null) {
            user.setGithub(request.getGithub());
        }
        if (request.getWebsite() != null) {
            user.setWebsite(request.getWebsite());
        }
        
        return userRepository.save(user);
    }
    
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "User not found with id: " + userId));
    }
}
