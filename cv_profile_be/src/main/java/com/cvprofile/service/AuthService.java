package com.cvprofile.service;

import com.cvprofile.dto.response.LoginResponse;
import com.cvprofile.dto.response.RegisterResponse;
import com.cvprofile.dto.request.LoginRequest;
import com.cvprofile.dto.request.RegisterRequest;
import com.cvprofile.entity.User;
import com.cvprofile.exception.ApiException;
import com.cvprofile.exception.ErrorCode;
import com.cvprofile.repository.UserRepository;
import com.cvprofile.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (!isValidEmail(request.getEmail())) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Email should be valid");
        }

        if (request.getPassword().length() < 6) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Password must be at least 6 characters");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiException(ErrorCode.EMAIL_EXISTS, "Email is already in use!");
        }
        
        String generatedUsername = generateUsernameFromEmail(request.getEmail());
        
        User user = new User();
        user.setUsername(generatedUsername);
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRole(User.Role.USER);
        user.setEnabled(true);
        
        User savedUser = userRepository.save(user);
        
        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getRole().name()
        );
    }
    
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "User not found with email: " + request.getEmail()));
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        request.getPassword()
                )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String token = tokenProvider.generateToken(authentication);
        
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setRole(user.getRole().name());
        
        return response;
    }
    
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "User not found"));
    }
    
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
    
    private String generateUsernameFromEmail(String email) {
        String baseUsername = email.substring(0, email.indexOf('@'))
                .replaceAll("[^a-zA-Z0-9]", "");
        
        if (baseUsername.length() < 3) {
            baseUsername = "user" + baseUsername;
        }
        
        String username = baseUsername;
        int counter = 1;
        while (userRepository.existsByUsername(username)) {
            username = baseUsername + counter;
            counter++;
        }
        
        return username;
    }
}
