package com.cvprofile.controller;

import com.cvprofile.entity.Event;
import com.cvprofile.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/profiles/{profileId}/events")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EventController {
    
    private final EventService eventService;
    
    @GetMapping
    public ResponseEntity<List<Event>> getEventsByProfileId(@PathVariable Long profileId) {
        return ResponseEntity.ok(eventService.getEventsByProfileId(profileId));
    }
    
    @PostMapping
    public ResponseEntity<Event> createEvent(@PathVariable Long profileId, @RequestBody Event event) {
        try {
            Event createdEvent = eventService.createEvent(profileId, event);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId, @RequestBody Event event) {
        try {
            Event updatedEvent = eventService.updateEvent(eventId, event);
            return ResponseEntity.ok(updatedEvent);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        try {
            eventService.deleteEvent(eventId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
