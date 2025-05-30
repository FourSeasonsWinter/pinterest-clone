package com.luiz.backend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.luiz.backend.dtos.PinDto;
import com.luiz.backend.entity.Pin;
import com.luiz.backend.entity.User;
import com.luiz.backend.mappers.PinMapper;
import com.luiz.backend.repository.FollowRepository;
import com.luiz.backend.repository.PinRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

  private final FollowRepository followRepository;
  private final PinRepository pinRepository;
  private final PinMapper pinMapper;

  @Override
  public Page<PinDto> getFeed(UUID userId, Pageable pageable) {
    List<User> followedUsers = followRepository.findFollowedByUserId(userId);
    Page<Pin> pinsPage = pinRepository.findByUsers(followedUsers, pageable);
    return pinsPage.map(pinMapper::toDto);
  }
}
