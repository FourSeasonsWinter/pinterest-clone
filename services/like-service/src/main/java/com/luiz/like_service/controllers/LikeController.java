package com.luiz.like_service.controllers;

import java.util.UUID;

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

import com.luiz.like_service.dtos.PageDto;
import com.luiz.like_service.dtos.PinDto;
import com.luiz.like_service.dtos.UserDto;
import com.luiz.like_service.services.LikeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {
  
  private final LikeService service;

  @Operation(summary = "Get users who liked a pin")
  @GetMapping("/pin/{pinId}")
  public ResponseEntity<PageDto<UserDto>> getUsersWhoLikedPin(
    @PathVariable UUID pinId,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size
  ) {
    return ResponseEntity.ok(service.getUsersWhoLikedThePin(pinId, PageRequest.of(page, size)));
  }

  @Operation(summary = "Get pins liked by a user")
  @GetMapping("/user/{userId}")
  public ResponseEntity<PageDto<PinDto>> getPinsLikedByUser(
    @PathVariable UUID userId,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size
  ) {
    return ResponseEntity.ok(service.getPinsLikedByTheUser(userId, PageRequest.of(page, size)));
  }

  @Operation(summary = "Give like to a pin")
  @SecurityRequirement(name = "bearerAuth")
  @PostMapping("/{pinId}")
  public ResponseEntity<Void> likePin(
    @PathVariable UUID pinId,
    @RequestHeader("X-User-Id") String userId
  ) {
    service.likePin(pinId, UUID.fromString(userId));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Operation(summary = "Remove like from a pin")
  @SecurityRequirement(name = "bearerAuth")
  @DeleteMapping("/{pinId}")
  public ResponseEntity<Void> unlikePin(
    @PathVariable UUID pinId,
    @RequestHeader("X-User-Id") String userId
  ) {
    service.unlikePin(pinId, UUID.fromString(userId));
    return ResponseEntity.noContent().build();
  }
}
