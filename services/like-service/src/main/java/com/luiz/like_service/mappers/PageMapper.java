package com.luiz.like_service.mappers;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import com.luiz.like_service.dtos.PageDto;
import com.luiz.like_service.dtos.PinDto;
import com.luiz.like_service.dtos.UserDto;

@Mapper(componentModel = "spring")
public interface PageMapper {
  PageDto<PinDto> toPinDto(Page<PinDto> page);
  PageDto<UserDto> toUserDto(Page<UserDto> users);
}
