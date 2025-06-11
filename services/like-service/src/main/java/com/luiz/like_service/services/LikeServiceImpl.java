package com.luiz.like_service.services;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.luiz.like_service.clients.PinServiceClient;
import com.luiz.like_service.clients.UserServiceClient;
import com.luiz.like_service.dtos.PageDto;
import com.luiz.like_service.dtos.PinDto;
import com.luiz.like_service.dtos.UserDto;
import com.luiz.like_service.entity.Like;
import com.luiz.like_service.mappers.PageMapper;
import com.luiz.like_service.repository.LikeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    
    private final LikeRepository likeRepository;
    private final PageMapper pageMapper;
    private final UserServiceClient userClient;
    private final PinServiceClient pinClient;
    
    @Override
    @Transactional
    public void likePin(UUID pinId, UUID userId) {
        var like = likeRepository.findByPinIdAndUserId(pinId, userId);
        if (like.isPresent()) {
            return;
        }
        
        Like newLike = new Like();
        newLike.setPinId(pinId);
        newLike.setUserId(userId);
        likeRepository.save(newLike);
    }
    
    @Override
    @Transactional
    public void unlikePin(UUID pinId, UUID userId) {
        var like = likeRepository.findByPinIdAndUserId(pinId, userId);
        
        if (like.isPresent()) {
            likeRepository.delete(like.get());
        }
    }
    
    @Override
    public PageDto<UserDto> getUsersWhoLikedThePin(UUID pinId, Pageable pageable) {
        Page<Like> likes = likeRepository.findByPinId(pinId, pageable);
        
        List<UUID> userIds = likes.stream()
            .map(Like::getUserId)
            .toList();
        
        List<UserDto> users = userClient.getUsersByIds(userIds);
        
        Map<UUID, UserDto> userMap = users.stream()
            .collect(Collectors.toMap(UserDto::getId, Function.identity()));
        List<UserDto> ordered = userIds.stream()
            .map(userMap::get)
            .toList();
        
        Page<UserDto> userPage = new PageImpl<>(ordered, pageable, likes.getTotalElements());
        return pageMapper.toUserDto(userPage);
    }
    
    @Override
    public PageDto<PinDto> getPinsLikedByTheUser(UUID userId, Pageable pageable) {
        Page<Like> likes = likeRepository.findByUserId(userId, pageable);
        List<UUID> pinIds = likes.stream().map(Like::getPinId).toList();
        List<PinDto> pins = pinClient.getPinsByIds(pinIds);
        Map<UUID, PinDto> pinMap = pins.stream()
            .collect(Collectors.toMap(PinDto::getId, Function.identity()));
        List<PinDto> ordered = pinIds.stream().map(pinMap::get).toList();

        Page<PinDto> pinPage = new PageImpl<>(ordered, pageable, likes.getTotalElements());
        return pageMapper.toPinDto(pinPage);
    }
}
