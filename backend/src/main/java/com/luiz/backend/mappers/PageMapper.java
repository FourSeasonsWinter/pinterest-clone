package com.luiz.backend.mappers;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import com.luiz.backend.dtos.PageDto;
import com.luiz.backend.dtos.PinDto;

@Mapper(componentModel = "spring")
public interface PageMapper {
  PageDto<PinDto> toDto(Page<PinDto> page);
}
