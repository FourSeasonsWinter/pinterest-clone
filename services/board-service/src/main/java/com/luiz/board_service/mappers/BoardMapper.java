package com.luiz.board_service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.luiz.board_service.dtos.BoardDto;
import com.luiz.board_service.dtos.BoardPostRequest;
import com.luiz.board_service.dtos.BoardUpdateRequest;
import com.luiz.board_service.entity.Board;

@Mapper(componentModel = "spring")
public interface BoardMapper {
  BoardDto toDto(Board board);
  Board toEntity(BoardPostRequest request);
  void update(BoardUpdateRequest request, @MappingTarget Board board);
}
