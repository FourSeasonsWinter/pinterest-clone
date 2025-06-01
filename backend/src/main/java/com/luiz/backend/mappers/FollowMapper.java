package com.luiz.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.luiz.backend.dtos.FollowDto;
import com.luiz.backend.entity.Follow;

@Mapper
public interface FollowMapper {

  @Mapping(source = "follower.id", target = "followerId")
  @Mapping(source = "followedBy.id", target = "followedById")
  FollowDto toDto(Follow follow);
}
