package com.luiz.backend.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luiz.backend.dtos.PageDto;
import com.luiz.backend.dtos.PinDto;
import com.luiz.backend.entity.User;
import com.luiz.backend.exception.UnauthenticatedException;
import com.luiz.backend.mappers.PageMapper;
import com.luiz.backend.repository.UserRepository;
import com.luiz.backend.services.FeedService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

  private final FeedService service;
  private final UserRepository userRepository;
  private final PageMapper pageMapper;

  @GetMapping
  @SecurityRequirement(name = "bearerAuth")
  public PageDto<PinDto> getFeed(
    @PageableDefault(size = 20, sort = "createdAt", direction = Direction.DESC) Pageable pageable,
    Authentication authentication
  ) {
    User user = getAuthenticatedUser(authentication);
    Page<PinDto> feed = service.getFeed(user.getId(), pageable);
    return pageMapper.toPinDto(feed);
  }

  private User getAuthenticatedUser(Authentication authentication) {
    if (authentication == null) {
      throw new UnauthenticatedException("No authenticated user");
    }

    String username = authentication.getName();
    return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username " + username));
  }
}
