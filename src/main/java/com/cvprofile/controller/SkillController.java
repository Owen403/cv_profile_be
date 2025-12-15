package com.cvprofile.controller;

import com.cvprofile.entity.Skill;
import com.cvprofile.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/profiles/{profileId}/skills")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SkillController {
    
    private final SkillService skillService;
    
    @GetMapping
    public ResponseEntity<List<Skill>> getSkillsByProfileId(@PathVariable Long profileId) {
        return ResponseEntity.ok(skillService.getSkillsByProfileId(profileId));
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Skill>> getSkillsByCategory(
            @PathVariable Long profileId, 
            @PathVariable String category) {
        return ResponseEntity.ok(skillService.getSkillsByProfileIdAndCategory(profileId, category));
    }
    
    @PostMapping
    public ResponseEntity<Skill> createSkill(@PathVariable Long profileId, @RequestBody Skill skill) {
        try {
            Skill createdSkill = skillService.createSkill(profileId, skill);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSkill);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{skillId}")
    public ResponseEntity<Skill> updateSkill(@PathVariable Long skillId, @RequestBody Skill skill) {
        try {
            Skill updatedSkill = skillService.updateSkill(skillId, skill);
            return ResponseEntity.ok(updatedSkill);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{skillId}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long skillId) {
        try {
            skillService.deleteSkill(skillId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
