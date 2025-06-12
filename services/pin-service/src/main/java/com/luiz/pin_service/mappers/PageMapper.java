package com.luiz.pin_service.mappers;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import com.luiz.pin_service.dtos.PageDto;
import com.luiz.pin_service.dtos.PinDto;

@Mapper(componentModel = "spring")
public interface PageMapper {
  PageDto<PinDto> toDto(Page<PinDto> page);
}
