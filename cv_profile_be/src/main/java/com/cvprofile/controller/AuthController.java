package com.cvprofile.controller;

import com.cvprofile.dto.response.ApiResponse;
import com.cvprofile.dto.response.LoginResponse;
import com.cvprofile.dto.response.RegisterResponse;
import com.cvprofile.dto.response.UserResponse;
import com.cvprofile.dto.request.LoginRequest;
import com.cvprofile.dto.request.RegisterRequest;
import com.cvprofile.entity.User;
import com.cvprofile.mapper.UserMapper;
import com.cvprofile.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final AuthService authService;
    private final UserMapper userMapper;
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", response));
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }
    
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        User user = authService.getCurrentUser();
        UserResponse response = userMapper.toResponse(user);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
