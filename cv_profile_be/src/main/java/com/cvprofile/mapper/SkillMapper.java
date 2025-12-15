package com.cvprofile.mapper;

import com.cvprofile.dto.request.SkillRequest;
import com.cvprofile.dto.response.SkillResponse;
import com.cvprofile.entity.Skill;
import com.cvprofile.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SkillMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "level", source = "request.proficiencyLevel")
    Skill toEntity(SkillRequest request, User user);
    
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "proficiencyLevel", source = "level")
    SkillResponse toResponse(Skill entity);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "level", source = "proficiencyLevel")
    void updateEntityFromRequest(SkillRequest request, @MappingTarget Skill entity);
}
