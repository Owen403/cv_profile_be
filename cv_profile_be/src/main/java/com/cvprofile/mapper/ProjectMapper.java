package com.cvprofile.mapper;

import com.cvprofile.dto.request.ProjectRequest;
import com.cvprofile.dto.response.ProjectResponse;
import com.cvprofile.entity.Project;
import com.cvprofile.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "role", source = "request.role")
    Project toEntity(ProjectRequest request, User user);
    
    @Mapping(target = "userId", source = "user.id")
    ProjectResponse toResponse(Project entity);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromRequest(ProjectRequest request, @MappingTarget Project entity);
}
