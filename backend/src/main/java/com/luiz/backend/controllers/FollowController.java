package com.luiz.backend.controllers;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luiz.backend.dtos.FollowDto;
import com.luiz.backend.dtos.PageDto;
import com.luiz.backend.dtos.UserDto;
import com.luiz.backend.entity.User;
import com.luiz.backend.services.FollowService;
import com.luiz.backend.util.GetUserUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
public class FollowController {

  private final FollowService service;
  private final GetUserUtil getUserUtil;

  @Operation(summary = "Get followers of a user")
  @GetMapping("/followers/{userId}")
  public ResponseEntity<PageDto<UserDto>> getFollowersOfUser(
    @PathVariable UUID userId,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size
  ) {
    return ResponseEntity.ok(service.getFollowersOfUser(userId, PageRequest.of(page, size)));
  }

  @Operation(summary = "Get users followed by a user")
  @GetMapping("/followedBy/{userId}")
  public ResponseEntity<PageDto<UserDto>> getUsersFollowedByUser(
    @PathVariable UUID userId,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size
  ) {
    return ResponseEntity.ok(service.getFollowedByUser(userId, PageRequest.of(page, size)));
  }

  @Operation(summary = "Follow a user")
  @SecurityRequirement(name = "bearerAuth")
  @PostMapping("/{followedById}")
  public ResponseEntity<FollowDto> followUser(
    @PathVariable UUID followedById,
    Authentication authentication
  ) {
    User user = getUserUtil.getAuthenticatedUser();
    FollowDto follow = service.followUser(followedById, user);

    return new ResponseEntity<>(follow, HttpStatus.CREATED);
  }

  @Operation(summary = "Unfollow a user")
  @SecurityRequirement(name = "bearerAuth")
  @DeleteMapping("/{followedById}")
  public ResponseEntity<Void> unfollowUser(
    @PathVariable UUID followedById,
    Authentication authentication
  ) {
    User user = getUserUtil.getAuthenticatedUser();
    service.unfollowUser(followedById, user);

    return ResponseEntity.noContent().build();
  }
}
