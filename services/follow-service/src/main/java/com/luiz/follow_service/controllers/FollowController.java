package com.luiz.follow_service.controllers;

import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luiz.follow_service.dtos.FollowDto;
import com.luiz.follow_service.dtos.PageDto;
import com.luiz.follow_service.dtos.UserDto;
import com.luiz.follow_service.mappers.PageMapper;
import com.luiz.follow_service.services.FollowService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
public class FollowController {

  private final FollowService service;
  private final PageMapper pageMapper;

  @Operation(summary = "Get followers of a user")
  @GetMapping("/followers/{userId}")
  public ResponseEntity<PageDto<UserDto>> getFollowers(
    @PathVariable UUID userId,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size
  ) {
    Page<UserDto> followers = service.getFollowers(userId, PageRequest.of(page, size));
    return ResponseEntity.ok(pageMapper.toDto(followers));
  }

  @Operation(summary = "Get users followed by a user")
  @GetMapping("/followed-by/{userId}")
  public ResponseEntity<PageDto<UserDto>> getFollowing(
    @PathVariable UUID userId,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size
  ) {
    Page<UserDto> following = service.getFollowing(userId, PageRequest.of(page, size));
    return ResponseEntity.ok(pageMapper.toDto(following));
  }

  @Operation(summary = "Get followers and following counts")
  @GetMapping("/count/{userId}")
  public ResponseEntity<Map<String, Long>> getFollowCounts(
    @PathVariable UUID userId
  ) {
    return ResponseEntity.ok(service.getFollowCounts(userId));
  }

  @Operation(summary = "Follow a user")
  @SecurityRequirement(name = "bearerAuth")
  @PostMapping("/{userToFollowId}")
  public ResponseEntity<FollowDto> followUser(
    @PathVariable UUID userToFollowId,
    @RequestHeader("X-User-Id") String authenticatedUserId
  ) {
    if (userToFollowId.equals(UUID.fromString(authenticatedUserId))) {
      return ResponseEntity.badRequest().build();
    }

    FollowDto follow = service.followUser(userToFollowId, UUID.fromString(authenticatedUserId));
    return new ResponseEntity<>(follow, HttpStatus.CREATED);
  }

  @Operation(summary = "Unfollow a user")
  @SecurityRequirement(name = "bearerAuth")
  @DeleteMapping("/{followedById}")
  public ResponseEntity<Void> unfollowUser(
    @PathVariable UUID followedById,
    @RequestHeader("X-User-Id") String authenticatedUserId
  ) {
    service.unfollowUser(followedById, UUID.fromString(authenticatedUserId));
    return ResponseEntity.noContent().build();
  }
}
