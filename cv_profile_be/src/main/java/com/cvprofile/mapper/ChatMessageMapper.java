package com.cvprofile.mapper;

import com.cvprofile.dto.request.ChatMessageRequest;
import com.cvprofile.dto.response.ChatMessageResponse;
import com.cvprofile.entity.ChatMessage;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamp", expression = "java(java.time.LocalDateTime.now())")
    ChatMessage toEntity(ChatMessageRequest request);
    
    ChatMessageResponse toResponse(ChatMessage entity);
}
