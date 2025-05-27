package com.luiz.backend.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luiz.backend.dtos.PageDto;
import com.luiz.backend.dtos.PinDto;
import com.luiz.backend.dtos.PinPostRequest;
import com.luiz.backend.entity.User;
import com.luiz.backend.exception.UnauthenticatedException;
import com.luiz.backend.mappers.PageMapper;
import com.luiz.backend.repository.UserRepository;
import com.luiz.backend.services.PinService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pins")
@RequiredArgsConstructor
public class PinController {
  
  private final PinService service;
  private final UserRepository userRepository;
  private final PageMapper pageMapper;

  @GetMapping("/{id}")
  public ResponseEntity<PinDto> getPin(@PathVariable UUID id) {
    PinDto pin = service.getPin(id);
    return ResponseEntity.ok(pin);
  }

  @GetMapping("/by-tag")
  public PageDto<PinDto> getPinsByTag(
    @RequestParam String tag,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size
  ) {
    Page<PinDto> pinsPage = service.getPinsByTag(tag, PageRequest.of(page, size));
    return pageMapper.toDto(pinsPage);
  }

  @GetMapping("/by-user")
  public PageDto<PinDto> getPinsByUser(
    @RequestParam UUID id,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size
  ) {
    // Can sort by createdAt or popularity with Sort.by("createdAt").descending() on PageRequest
    Page<PinDto> pinsPage = service.getPinsByUser(id, PageRequest.of(page, size));
    return pageMapper.toDto(pinsPage);
  }

  @PostMapping
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<PinDto> createPin(
    @RequestBody PinPostRequest request,
    Authentication authentication
  ) {
    User user = getAuthenticatedUser(authentication);
    return ResponseEntity.ok(service.createPin(request, user));
  }

  @DeleteMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> deletePin(
    @PathVariable UUID id,
    Authentication authentication
  ) {
    User user = getAuthenticatedUser(authentication);
    service.deletePin(id, user);

    return ResponseEntity.noContent().build();
  }

  private User getAuthenticatedUser(Authentication authentication) {
    if (authentication == null) {
      throw new UnauthenticatedException("There is no logged in user");
    }

    String username = authentication.getName();
    return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username " + username));
  }
}
