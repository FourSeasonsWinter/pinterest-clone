package com.luiz.backend.util;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.luiz.backend.entity.User;
import com.luiz.backend.exception.UnauthenticatedException;
import com.luiz.backend.exception.UserNotFoundException;
import com.luiz.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetUserUtil {

  private final UserRepository userRepository;

  public User getAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null) {
      throw new UnauthenticatedException("There is no logged in user");
    }

    String username = authentication.getName();
    return getUserByUsername(username);
  }

  public User getUserByUsername(String username) {
    return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username " + username));
  }

  public User getUserById(UUID id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
  }
}
