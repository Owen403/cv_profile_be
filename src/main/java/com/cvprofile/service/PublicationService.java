package com.cvprofile.service;

import com.cvprofile.entity.Publication;
import com.cvprofile.entity.Profile;
import com.cvprofile.exception.ApiException;
import com.cvprofile.exception.ErrorCode;
import com.cvprofile.repository.PublicationRepository;
import com.cvprofile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicationService {
    
    private final PublicationRepository publicationRepository;
    private final ProfileRepository profileRepository;
    
    @Transactional(readOnly = true)
    public List<Publication> getPublicationsByProfileId(Long profileId) {
        return publicationRepository.findByProfileIdOrderByPublishedDateDesc(profileId);
    }
    
    @Transactional
    public Publication createPublication(Long profileId, Publication publication) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ApiException(ErrorCode.PROFILE_NOT_FOUND, "Profile not found with id: " + profileId));
        
        publication.setProfile(profile);
        return publicationRepository.save(publication);
    }
    
    @Transactional
    public Publication updatePublication(Long id, Publication publicationDetails) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.PUBLICATION_NOT_FOUND, "Publication not found with id: " + id));
        
        publication.setTitle(publicationDetails.getTitle());
        publication.setAbstractText(publicationDetails.getAbstractText());
        publication.setAuthors(publicationDetails.getAuthors());
        publication.setJournal(publicationDetails.getJournal());
        publication.setConference(publicationDetails.getConference());
        publication.setPublishedDate(publicationDetails.getPublishedDate());
        publication.setDoi(publicationDetails.getDoi());
        publication.setUrl(publicationDetails.getUrl());
        publication.setCitation(publicationDetails.getCitation());
        
        return publicationRepository.save(publication);
    }
    
    @Transactional
    public void deletePublication(Long id) {
        if (!publicationRepository.existsById(id)) {
            throw new ApiException(ErrorCode.PUBLICATION_NOT_FOUND, "Publication not found with id: " + id);
        }
        publicationRepository.deleteById(id);
    }
}
