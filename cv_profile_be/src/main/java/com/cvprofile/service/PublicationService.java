package com.cvprofile.service;

import com.cvprofile.dto.request.PublicationRequest;
import com.cvprofile.dto.response.PublicationResponse;
import com.cvprofile.entity.Publication;
import com.cvprofile.entity.User;
import com.cvprofile.exception.ApiException;
import com.cvprofile.exception.ErrorCode;
import com.cvprofile.mapper.PublicationMapper;
import com.cvprofile.repository.PublicationRepository;
import com.cvprofile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicationService {
    
    private final PublicationRepository publicationRepository;
    private final UserRepository userRepository;
    private final PublicationMapper publicationMapper;
    
    @Transactional(readOnly = true)
    public List<PublicationResponse> getPublicationsByUserId(Long userId) {
        return publicationRepository.findByUserIdOrderByPublishedDateDesc(userId).stream()
                .map(publicationMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public PublicationResponse createPublication(Long userId, PublicationRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "User not found with id: " + userId));
        
        Publication publication = publicationMapper.toEntity(request, user);
        Publication saved = publicationRepository.save(publication);
        return publicationMapper.toResponse(saved);
    }
    
    @Transactional
    public PublicationResponse updatePublication(Long id, PublicationRequest request) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.PUBLICATION_NOT_FOUND, "Publication not found with id: " + id));
        
        publicationMapper.updateEntityFromRequest(request, publication);
        Publication updated = publicationRepository.save(publication);
        return publicationMapper.toResponse(updated);
    }
    
    @Transactional
    public void deletePublication(Long id) {
        if (!publicationRepository.existsById(id)) {
            throw new ApiException(ErrorCode.PUBLICATION_NOT_FOUND, "Publication not found with id: " + id);
        }
        publicationRepository.deleteById(id);
    }
}
