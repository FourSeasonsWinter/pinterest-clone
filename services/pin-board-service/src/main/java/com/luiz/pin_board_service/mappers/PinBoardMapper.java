package com.luiz.pin_board_service.mappers;

import org.mapstruct.Mapper;

import com.luiz.pin_board_service.dtos.PinBoardDto;
import com.luiz.pin_board_service.entity.PinBoard;

@Mapper(componentModel = "spring")
public interface PinBoardMapper {
  PinBoardDto toDto(PinBoard pinBoard);
}
