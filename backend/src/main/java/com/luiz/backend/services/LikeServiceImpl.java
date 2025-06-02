package com.luiz.backend.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.luiz.backend.dtos.PageDto;
import com.luiz.backend.dtos.PinDto;
import com.luiz.backend.dtos.UserDto;
import com.luiz.backend.entity.Like;
import com.luiz.backend.entity.Pin;
import com.luiz.backend.entity.User;
import com.luiz.backend.exception.PinNotFoundException;
import com.luiz.backend.mappers.PageMapper;
import com.luiz.backend.mappers.PinMapper;
import com.luiz.backend.mappers.UserMapper;
import com.luiz.backend.repository.LikeRepository;
import com.luiz.backend.repository.PinRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

  private final LikeRepository likeRepository;
  private final PinRepository pinRepository;
  private final PinMapper pinMapper;
  private final UserMapper userMapper;
  private final PageMapper pageMapper;

  @Override
  @Transactional
  public void likePin(UUID pinId, User user) {
    Pin pin = pinRepository.findById(pinId).orElseThrow(() -> new PinNotFoundException("Pin not found with id " + pinId));
    pin.addLike();
    pinRepository.save(pin);

    Like like = new Like();
    like.setPin(pin);
    like.setUser(user);
    likeRepository.save(like);
  }

  @Override
  @Transactional
  public void unlikePin(UUID pinId, User user) {
    Pin pin = pinRepository.findById(pinId).orElseThrow(() -> new PinNotFoundException("Pin not found with id " + pinId));
    pin.removeLike();
    pinRepository.save(pin);

    Like like = likeRepository.findByPinIdAndUserId(pinId, user.getId());
    likeRepository.delete(like);
  }

  @Override
  public PageDto<UserDto> getUsersWhoLikedThePin(UUID pinId, Pageable pageable) {
    Page<User> users = likeRepository.findUsersByPinId(pinId, pageable);
    Page<UserDto> dtos = users.map(userMapper::toDto);
    return pageMapper.toUserDto(dtos);
  }

  @Override
  public PageDto<PinDto> getPinsLikedByTheUser(UUID userId, Pageable pageable) {
    Page<Pin> pins = likeRepository.findPinsByUserId(userId, pageable);
    Page<PinDto> dtos = pins.map(pinMapper::toDto);
    return pageMapper.toPinDto(dtos);
  }
}
