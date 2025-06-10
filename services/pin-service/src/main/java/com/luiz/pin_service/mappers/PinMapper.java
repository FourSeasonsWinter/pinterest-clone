package com.luiz.pin_service.mappers;

import org.mapstruct.Mapper;

import com.luiz.pin_service.dtos.PinDto;
import com.luiz.pin_service.dtos.PinPostRequest;
import com.luiz.pin_service.entity.Pin;

@Mapper(componentModel = "spring")
public interface PinMapper {
  PinDto toDto(Pin pin);
  Pin toEntity(PinPostRequest request);
  Pin toEntity(PinDto dto);
}
