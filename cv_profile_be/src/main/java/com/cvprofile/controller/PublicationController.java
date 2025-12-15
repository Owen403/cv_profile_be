package com.cvprofile.controller;

import com.cvprofile.dto.request.PublicationRequest;
import com.cvprofile.dto.response.ApiResponse;
import com.cvprofile.dto.response.PublicationResponse;
import com.cvprofile.service.PublicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/publications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PublicationController {
    
    private final PublicationService publicationService;
    
    @GetMapping
    public ApiResponse<List<PublicationResponse>> getPublicationsByUserId(@PathVariable Long userId) {
        return ApiResponse.<List<PublicationResponse>>builder()
                .success(true)
                .message("Get publications successfully")
                .data(publicationService.getPublicationsByUserId(userId))
                .build();
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PublicationResponse> createPublication(@PathVariable Long userId, @Valid @RequestBody PublicationRequest request) {
        return ApiResponse.<PublicationResponse>builder()
                .success(true)
                .message("Create publication successfully")
                .data(publicationService.createPublication(userId, request))
                .build();
    }
    
    @PutMapping("/{publicationId}")
    public ApiResponse<PublicationResponse> updatePublication(@PathVariable Long publicationId, @Valid @RequestBody PublicationRequest request) {
        return ApiResponse.<PublicationResponse>builder()
                .success(true)
                .message("Update publication successfully")
                .data(publicationService.updatePublication(publicationId, request))
                .build();
    }
    
    @DeleteMapping("/{publicationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePublication(@PathVariable Long publicationId) {
        publicationService.deletePublication(publicationId);
    }
}
