package com.luiz.like_service.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.luiz.like_service.dtos.PageDto;
import com.luiz.like_service.dtos.PinDto;
import com.luiz.like_service.dtos.UserDto;

public interface LikeService {
  void likePin(UUID pinId, UUID userId);
  void unlikePin(UUID pinId, UUID userId);
  PageDto<UserDto> getUsersWhoLikedThePin(UUID pinId, Pageable pageable);
  PageDto<PinDto> getPinsLikedByUser(UUID userId, Pageable pageable);
}
