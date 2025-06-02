package com.luiz.backend.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.luiz.backend.dtos.FollowDto;
import com.luiz.backend.dtos.PageDto;
import com.luiz.backend.dtos.UserDto;
import com.luiz.backend.entity.Follow;
import com.luiz.backend.entity.User;
import com.luiz.backend.mappers.FollowMapper;
import com.luiz.backend.mappers.PageMapper;
import com.luiz.backend.mappers.UserMapper;
import com.luiz.backend.repository.FollowRepository;
import com.luiz.backend.util.GetUserUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

  private final FollowRepository followRepository;
  private final FollowMapper followMapper;
  private final UserMapper userMapper;
  private final PageMapper pageMapper;
  private final GetUserUtil getUserUtil;

  @Override
  public PageDto<UserDto> getFollowersOfUser(UUID userId, Pageable pageable) {
    Page<User> followers = followRepository.findFollowersByUserId(userId, pageable);
    Page<UserDto> dtos = followers.map(userMapper::toDto);

    return pageMapper.toUserDto(dtos);
  }

  @Override
  public PageDto<UserDto> getFollowingByUser(UUID userId, Pageable pageable) {
    Page<User> following = followRepository.findFollowingByUserId(userId, pageable);
    Page<UserDto> dtos = following.map(userMapper::toDto);

    return pageMapper.toUserDto(dtos);
  }

  @Override
  public FollowDto followUser(UUID userToFollowId, User authenticatedUser) {
    User userFollowedBy = getUserUtil.getUserById(userToFollowId);
    Follow follow = new Follow();
    follow.setFollower(authenticatedUser);
    follow.setFollowedBy(userFollowedBy);
    followRepository.save(follow);
    
    return followMapper.toDto(follow);
  }

  @Override
  public void unfollowUser(UUID userToUnfollowId, User authenticatedUser) {
    Follow follow = followRepository.findByFollowerIdAndFollowedById(authenticatedUser.getId(), userToUnfollowId);
    followRepository.delete(follow);
  }
}
