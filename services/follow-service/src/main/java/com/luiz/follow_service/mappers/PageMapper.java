package com.luiz.follow_service.mappers;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import com.luiz.follow_service.dtos.PageDto;
import com.luiz.follow_service.dtos.UserDto;

@Mapper(componentModel = "spring")
public interface PageMapper {
  PageDto<UserDto> toDto(Page<UserDto> page);
}
