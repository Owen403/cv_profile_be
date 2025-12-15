package com.cvprofile.controller;

import com.cvprofile.dto.request.EventRequest;
import com.cvprofile.dto.response.ApiResponse;
import com.cvprofile.dto.response.EventResponse;
import com.cvprofile.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/events")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EventController {
    
    private final EventService eventService;
    
    @GetMapping
    public ApiResponse<List<EventResponse>> getEventsByUserId(@PathVariable Long userId) {
        return ApiResponse.<List<EventResponse>>builder()
                .success(true)
                .message("Get events successfully")
                .data(eventService.getEventsByUserId(userId))
                .build();
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<EventResponse> createEvent(@PathVariable Long userId, @Valid @RequestBody EventRequest request) {
        return ApiResponse.<EventResponse>builder()
                .success(true)
                .message("Create event successfully")
                .data(eventService.createEvent(userId, request))
                .build();
    }
    
    @PutMapping("/{eventId}")
    public ApiResponse<EventResponse> updateEvent(@PathVariable Long eventId, @Valid @RequestBody EventRequest request) {
        return ApiResponse.<EventResponse>builder()
                .success(true)
                .message("Update event successfully")
                .data(eventService.updateEvent(eventId, request))
                .build();
    }
    
    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
    }
}
