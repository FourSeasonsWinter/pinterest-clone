package com.luiz.backend.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.luiz.backend.dtos.PageDto;
import com.luiz.backend.dtos.PinDto;
import com.luiz.backend.dtos.UserDto;
import com.luiz.backend.entity.User;

public interface LikeService {
  void likePin(UUID pinId, User user);
  void dislikePin(UUID pinId, User user);
  PageDto<UserDto> getUsersWhoLikedThePin(UUID pinId, Pageable pageable);
  PageDto<PinDto> getPinsLikedByTheUser(UUID userId, Pageable pageable);
}
