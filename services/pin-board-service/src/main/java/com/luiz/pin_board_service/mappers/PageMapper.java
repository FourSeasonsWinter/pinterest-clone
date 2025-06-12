package com.luiz.pin_board_service.mappers;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import com.luiz.pin_board_service.dtos.BoardDto;
import com.luiz.pin_board_service.dtos.PageDto;
import com.luiz.pin_board_service.dtos.PinDto;
import com.luiz.pin_board_service.dtos.UserDto;

@Mapper(componentModel = "spring")
public interface PageMapper {
  PageDto<PinDto> toPinDto(Page<PinDto> page);
  PageDto<UserDto> toUserDto(Page<UserDto> users);
  PageDto<BoardDto> toBoardDto(Page<BoardDto> boards);
}
