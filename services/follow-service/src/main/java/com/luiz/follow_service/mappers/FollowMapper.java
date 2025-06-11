package com.luiz.follow_service.mappers;

import org.mapstruct.Mapper;

import com.luiz.follow_service.dtos.FollowDto;
import com.luiz.follow_service.entity.Follow;

@Mapper(componentModel = "spring")
public interface FollowMapper {
  FollowDto toDto(Follow follow);
}
