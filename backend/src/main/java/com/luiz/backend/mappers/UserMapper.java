package com.luiz.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.luiz.backend.dtos.UserDto;
import com.luiz.backend.dtos.UserUpdateRequest;
import com.luiz.backend.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserDto toDto(User user);
  
  void update(UserUpdateRequest request, @MappingTarget User user);
}
