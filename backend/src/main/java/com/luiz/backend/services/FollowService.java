package com.luiz.backend.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.luiz.backend.dtos.FollowDto;
import com.luiz.backend.dtos.PageDto;
import com.luiz.backend.dtos.UserDto;
import com.luiz.backend.entity.User;

public interface FollowService {
  PageDto<UserDto> getFollowersOfUser(UUID userId, Pageable pageable);
  PageDto<UserDto> getFollowingByUser(UUID userId, Pageable pageable);
  FollowDto followUser(UUID userToFollowId, User authenticatedUser);
  void unfollowUser(UUID userToUnfollowId, User authenticatedUser);
}
