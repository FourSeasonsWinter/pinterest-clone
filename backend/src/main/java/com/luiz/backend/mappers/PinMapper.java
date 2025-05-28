package com.luiz.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.luiz.backend.dtos.PinDto;
import com.luiz.backend.dtos.PinPostRequest;
import com.luiz.backend.entity.Pin;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PinMapper {
  @Mapping(source = "user.id", target = "userId")
  PinDto toDto(Pin pin);
  Pin toEntity(PinPostRequest request);
  Pin toEntity(PinDto dto);
}
