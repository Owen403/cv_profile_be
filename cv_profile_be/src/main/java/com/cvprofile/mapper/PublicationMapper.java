package com.cvprofile.mapper;

import com.cvprofile.dto.request.PublicationRequest;
import com.cvprofile.dto.response.PublicationResponse;
import com.cvprofile.entity.Publication;
import com.cvprofile.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PublicationMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "publishedDate", source = "request.publishDate")
    @Mapping(target = "citation", ignore = true)
    @Mapping(target = "abstractText", ignore = true)
    @Mapping(target = "conference", ignore = true)
    Publication toEntity(PublicationRequest request, User user);
    
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "publishDate", source = "publishedDate")
    @Mapping(target = "citations", expression = "java(0)")
    PublicationResponse toResponse(Publication entity);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "publishedDate", source = "publishDate")
    @Mapping(target = "citation", ignore = true)
    @Mapping(target = "abstractText", ignore = true)
    @Mapping(target = "conference", ignore = true)
    void updateEntityFromRequest(PublicationRequest request, @MappingTarget Publication entity);
}
