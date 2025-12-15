package com.cvprofile.controller;

import com.cvprofile.entity.Publication;
import com.cvprofile.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/profiles/{profileId}/publications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PublicationController {
    
    private final PublicationService publicationService;
    
    @GetMapping
    public ResponseEntity<List<Publication>> getPublicationsByProfileId(@PathVariable Long profileId) {
        return ResponseEntity.ok(publicationService.getPublicationsByProfileId(profileId));
    }
    
    @PostMapping
    public ResponseEntity<Publication> createPublication(@PathVariable Long profileId, @RequestBody Publication publication) {
        try {
            Publication createdPublication = publicationService.createPublication(profileId, publication);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPublication);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{publicationId}")
    public ResponseEntity<Publication> updatePublication(@PathVariable Long publicationId, @RequestBody Publication publication) {
        try {
            Publication updatedPublication = publicationService.updatePublication(publicationId, publication);
            return ResponseEntity.ok(updatedPublication);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{publicationId}")
    public ResponseEntity<Void> deletePublication(@PathVariable Long publicationId) {
        try {
            publicationService.deletePublication(publicationId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
