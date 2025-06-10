package com.luiz.user_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luiz.user_service.dtos.UserDto;
import com.luiz.user_service.dtos.UserPasswordUpdateRequest;
import com.luiz.user_service.dtos.UserUpdateRequest;
import com.luiz.user_service.entity.User;
import com.luiz.user_service.exceptions.UnauthenticatedException;
import com.luiz.user_service.exceptions.UserNotFoundException;
import com.luiz.user_service.repository.UserRepository;
import com.luiz.user_service.services.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

  private final UserService service;
  private final UserRepository repository;
  private final PasswordEncoder encoder;

  @GetMapping("/me")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
    User user = getAuthenticatedUser(authentication);
    return ResponseEntity.ok(service.getCurrentUser(user));
  }

  @GetMapping("/{username}")
  public ResponseEntity<UserDto> getUser(@PathVariable String username) {
    UserDto user = service.getUser(username);
    return ResponseEntity.ok(user);
  }

  @PutMapping
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<UserDto> updateUser(
      @RequestBody UserUpdateRequest request,
      Authentication authentication) {
    User user = getAuthenticatedUser(authentication);
    return ResponseEntity.ok(service.updateCurrentUser(request, user));
  }

  @PostMapping("/change-password")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<String> changePassword(
      @RequestBody UserPasswordUpdateRequest request,
      Authentication authentication) {
    User user = getAuthenticatedUser(authentication);

    if (!encoder.matches(request.getOldPassword(), user.getPassword())) {
      return ResponseEntity.badRequest().body("Wrong old password");
    }

    service.changePassword(request, user);

    return ResponseEntity.ok("Password changed");
  }

  private User getAuthenticatedUser(Authentication authentication) {
    if (authentication == null) {
      throw new UnauthenticatedException("User is not logged in");
    }

    String username = authentication.getName();
    return repository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException("User not found with username " + username));
  }
}
