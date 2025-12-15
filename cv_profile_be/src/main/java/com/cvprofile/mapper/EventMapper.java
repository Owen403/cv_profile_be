package com.cvprofile.mapper;

import com.cvprofile.dto.request.EventRequest;
import com.cvprofile.dto.response.EventResponse;
import com.cvprofile.entity.Event;
import com.cvprofile.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EventMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "role", source = "request.role")
    @Mapping(target = "title", source = "request.name")
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "eventType", ignore = true)
    @Mapping(target = "startDate", source = "request.eventDate")
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "certificate", ignore = true)
    @Mapping(target = "url", ignore = true)
    Event toEntity(EventRequest request, User user);
    
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "name", source = "title")
    @Mapping(target = "eventDate", source = "startDate")
    EventResponse toResponse(Event entity);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "title", source = "name")
    @Mapping(target = "startDate", source = "eventDate")
    @Mapping(target = "endDate", ignore = true)
    void updateEntityFromRequest(EventRequest request, @MappingTarget Event entity);
}
