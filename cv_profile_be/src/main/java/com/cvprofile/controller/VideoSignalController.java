package com.cvprofile.controller;

import com.cvprofile.dto.request.VideoSignalRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class VideoSignalController {
    
    @MessageMapping("/video.signal")
    @SendTo("/topic/video-signal")
    public VideoSignalRequest handleVideoSignal(VideoSignalRequest request,
                                               SimpMessageHeaderAccessor headerAccessor) {
        // Forward signaling data to all connected clients
        return request;
    }
    
    @MessageMapping("/video.offer")
    @SendTo("/topic/video-signal")
    public VideoSignalRequest handleOffer(VideoSignalRequest request) {
        return request;
    }
    
    @MessageMapping("/video.answer")
    @SendTo("/topic/video-signal")
    public VideoSignalRequest handleAnswer(VideoSignalRequest request) {
        return request;
    }
}
