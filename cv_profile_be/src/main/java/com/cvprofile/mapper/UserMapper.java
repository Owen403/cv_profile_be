package com.cvprofile.mapper;

import com.cvprofile.dto.response.UserResponse;
import com.cvprofile.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
}
