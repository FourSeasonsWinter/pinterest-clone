package com.luiz.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.luiz.backend.dtos.PinBoardDto;
import com.luiz.backend.entity.PinBoard;

@Mapper(componentModel = "spring")
public interface PinBoardMapper {
  @Mapping(source = "pin.id", target = "pinId")
  @Mapping(source = "board.id", target = "boardId")
  PinBoardDto toDto(PinBoard pinBoard);
}
