package com.luiz.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.luiz.backend.dtos.BoardDto;
import com.luiz.backend.dtos.BoardPostRequest;
import com.luiz.backend.dtos.BoardUpdateRequest;
import com.luiz.backend.entity.Board;

@Mapper(componentModel = "spring", uses = {PinMapper.class, UserMapper.class})
public interface BoardMapper {
  @Mapping(source = "user.id", target = "userId")
  @Mapping(source = "user.username", target = "username")
  BoardDto toDto(Board board);
  
  Board toEntity(BoardPostRequest request);
  void update(BoardUpdateRequest request, @MappingTarget Board board);
}
