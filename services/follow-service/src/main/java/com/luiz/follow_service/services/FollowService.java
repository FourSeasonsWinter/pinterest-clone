package com.luiz.follow_service.services;

import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.luiz.follow_service.dtos.FollowDto;
import com.luiz.follow_service.dtos.UserDto;

public interface FollowService {
  FollowDto followUser(UUID userToFollowId, UUID authenticatedUserId);
  void unfollowUser(UUID userToUnfollowId, UUID authenticatedUserId);
  Page<UserDto> getFollowers(UUID userId, Pageable pageable);
  Page<UserDto> getFollowing(UUID userId, Pageable pageable);
  Map<String, Long> getFollowCounts(UUID userId);
}
