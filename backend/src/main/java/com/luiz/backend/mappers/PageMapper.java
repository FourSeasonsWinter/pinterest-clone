package com.luiz.backend.mappers;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import com.luiz.backend.dtos.BoardDto;
import com.luiz.backend.dtos.PageDto;
import com.luiz.backend.dtos.PinDto;
import com.luiz.backend.dtos.UserDto;

@Mapper(componentModel = "spring")
public interface PageMapper {
  PageDto<PinDto> toPinDto(Page<PinDto> page);
  PageDto<BoardDto> toBoardDto(Page<BoardDto> boards);
  PageDto<UserDto> toUserDto(Page<UserDto> users);
}
