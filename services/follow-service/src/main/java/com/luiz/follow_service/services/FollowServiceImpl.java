package com.luiz.follow_service.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.luiz.follow_service.clients.UserServiceClient;
import com.luiz.follow_service.dtos.FollowDto;
import com.luiz.follow_service.dtos.UserDto;
import com.luiz.follow_service.entity.Follow;
import com.luiz.follow_service.mappers.FollowMapper;
import com.luiz.follow_service.repository.FollowRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {
    
    private final FollowRepository followRepository;
    private final FollowMapper followMapper;
    private final UserServiceClient userClient;
    
    @Override
    public FollowDto followUser(UUID userToFollowId, UUID authenticatedUserId) {
        Follow follow = new Follow();
        follow.setFollowerId(authenticatedUserId);
        follow.setFollowedById(userToFollowId);
        followRepository.save(follow);
        
        return followMapper.toDto(follow);
    }
    
    @Override
    public void unfollowUser(UUID userToUnfollowId, UUID authenticatedUserId) {
        Follow follow = followRepository.findByFollowerIdAndFollowedById(authenticatedUserId, userToUnfollowId);
        followRepository.delete(follow);
    }
    
    @Override
    public Page<UserDto> getFollowers(UUID userId, Pageable pageable) {
        Page<Follow> follows = followRepository.findByFollowedById(userId, pageable);
        List<UUID> followerIds = follows.stream().map(Follow::getFollowerId).collect(Collectors.toList());
        
        List<UserDto> users = userClient.getUsersByIds(followerIds);
        
        Map<UUID, UserDto> userMap = users.stream().collect(Collectors.toMap(UserDto::getId, Function.identity()));
        List<UserDto> ordered = followerIds.stream().map(userMap::get).collect(Collectors.toList());
        
        return new PageImpl<>(ordered, pageable, follows.getTotalElements());
    }
    
    @Override
    public Page<UserDto> getFollowing(UUID userId, Pageable pageable) {
        Page<Follow> follows = followRepository.findByFollowerId(userId, pageable);
        List<UUID> followedIds = follows.stream().map(Follow::getFollowedById).collect(Collectors.toList());

        List<UserDto> users = userClient.getUsersByIds(followedIds);

        Map<UUID, UserDto> userMap = users.stream().collect(Collectors.toMap(UserDto::getId, Function.identity()));
        List<UserDto> ordered = followedIds.stream().map(userMap::get).collect(Collectors.toList());

        return new PageImpl<>(ordered, pageable, follows.getTotalElements());
    }

    @Override
    public Map<String, Long> getFollowCounts(UUID userId) {
        long followers = followRepository.countByFollowedById(userId);
        long following = followRepository.countByFollowerId(userId);

        Map<String, Long> map = new HashMap<>();
        map.put("followers", followers);
        map.put("following", following);
        return map;
    }
}