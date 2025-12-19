package com.realestate.user.mapper;

import com.realestate.common.dto.UserResponse;
import com.realestate.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * MapStruct mapper for User entity toDTO conversions
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserResponse toUserResponse(User user);
}
