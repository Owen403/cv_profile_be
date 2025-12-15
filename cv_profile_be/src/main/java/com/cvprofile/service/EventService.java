package com.cvprofile.service;

import com.cvprofile.dto.request.EventRequest;
import com.cvprofile.dto.response.EventResponse;
import com.cvprofile.entity.Event;
import com.cvprofile.entity.User;
import com.cvprofile.exception.ApiException;
import com.cvprofile.exception.ErrorCode;
import com.cvprofile.mapper.EventMapper;
import com.cvprofile.repository.EventRepository;
import com.cvprofile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    
    @Transactional(readOnly = true)
    public List<EventResponse> getEventsByUserId(Long userId) {
        return eventRepository.findByUserIdOrderByStartDateDesc(userId).stream()
                .map(eventMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public EventResponse createEvent(Long userId, EventRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "User not found with id: " + userId));
        
        Event event = eventMapper.toEntity(request, user);
        Event saved = eventRepository.save(event);
        return eventMapper.toResponse(saved);
    }
    
    @Transactional
    public EventResponse updateEvent(Long id, EventRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND, "Event not found with id: " + id));
        
        eventMapper.updateEntityFromRequest(request, event);
        Event updated = eventRepository.save(event);
        return eventMapper.toResponse(updated);
    }
    
    @Transactional
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ApiException(ErrorCode.EVENT_NOT_FOUND, "Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }
}
