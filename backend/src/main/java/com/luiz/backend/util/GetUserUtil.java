package com.luiz.backend.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.luiz.backend.entity.User;
import com.luiz.backend.exception.UnauthenticatedException;
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
    return getUser(username);
  }

  public User getUser(String username) {
    return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username " + username));
  }
}
