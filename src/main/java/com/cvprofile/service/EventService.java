package com.cvprofile.service;

import com.cvprofile.entity.Event;
import com.cvprofile.entity.Profile;
import com.cvprofile.repository.EventRepository;
import com.cvprofile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    
    private final EventRepository eventRepository;
    private final ProfileRepository profileRepository;
    
    @Transactional(readOnly = true)
    public List<Event> getEventsByProfileId(Long profileId) {
        return eventRepository.findByProfileIdOrderByStartDateDesc(profileId);
    }
    
    @Transactional
    public Event createEvent(Long profileId, Event event) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));
        
        event.setProfile(profile);
        return eventRepository.save(event);
    }
    
    @Transactional
    public Event updateEvent(Long id, Event eventDetails) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        
        event.setTitle(eventDetails.getTitle());
        event.setDescription(eventDetails.getDescription());
        event.setEventType(eventDetails.getEventType());
        event.setLocation(eventDetails.getLocation());
        event.setStartDate(eventDetails.getStartDate());
        event.setEndDate(eventDetails.getEndDate());
        event.setRole(eventDetails.getRole());
        event.setCertificate(eventDetails.getCertificate());
        event.setUrl(eventDetails.getUrl());
        
        return eventRepository.save(event);
    }
    
    @Transactional
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }
}
