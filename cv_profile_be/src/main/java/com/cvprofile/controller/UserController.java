package com.cvprofile.controller;

import com.cvprofile.dto.request.UpdateProfileRequest;
import com.cvprofile.dto.response.ApiResponse;
import com.cvprofile.dto.response.UserResponse;
import com.cvprofile.entity.User;
import com.cvprofile.mapper.UserMapper;
import com.cvprofile.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;
    private final UserMapper userMapper;
    
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        UserResponse response = userMapper.toResponse(user);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateProfileRequest request) {
        User user = userService.updateProfile(userId, request);
        UserResponse response = userMapper.toResponse(user);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", response));
    }
}
