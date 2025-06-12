package com.luiz.user_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.luiz.user_service.dtos.UserDto;
import com.luiz.user_service.dtos.UserUpdateRequest;
import com.luiz.user_service.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserDto toDto(User user);

  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "password", ignore = true)
  @Mapping(target = "profilePictureUrl", ignore = true)
  void update(UserUpdateRequest request, @MappingTarget User user);
}
