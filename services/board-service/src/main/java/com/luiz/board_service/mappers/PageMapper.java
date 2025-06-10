package com.luiz.board_service.mappers;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import com.luiz.board_service.dtos.PageDto;
import com.luiz.board_service.dtos.BoardDto;

@Mapper(componentModel = "spring")
public interface PageMapper {
  PageDto<BoardDto> toDto(Page<BoardDto> page);
}
