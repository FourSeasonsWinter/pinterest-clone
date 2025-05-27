package com.luiz.backend.mappers;

import org.mapstruct.Mapper;

import com.luiz.backend.dtos.PinDto;
import com.luiz.backend.dtos.PinPostRequest;
import com.luiz.backend.entity.Pin;

@Mapper(componentModel = "spring")
public interface PinMapper {
  PinDto toDto(Pin pin);
  Pin toEntity(PinPostRequest request);
}
