package com.luiz.user_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luiz.user_service.dtos.LoginRequest;
import com.luiz.user_service.dtos.RegisterRequest;
import com.luiz.user_service.entity.User;
import com.luiz.user_service.exceptions.InvalidCredentialsException;
import com.luiz.user_service.exceptions.UserNotFoundException;
import com.luiz.user_service.repository.UserRepository;
import com.luiz.user_service.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  
  private final UserRepository repository;
  private final PasswordEncoder encoder;
  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;

  // TODO add a refresh token mechanism
  @PostMapping("/login")
  public String login(@RequestBody LoginRequest request) {
    String username = request.getUsername();
    User user = repository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found with username " + username));

    try {
      authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(username, request.getPassword())
      );
      return jwtUtil.generateToken(user.getId());
    } catch (Exception e) {
      throw new InvalidCredentialsException("Invalid credentials");
    }
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
    String username = request.getUsername();
    String password = request.getPassword();

    if (repository.findByUsername(username).isPresent()) {
      return ResponseEntity.badRequest().body("Username is already taken");
    }

    String hashedPassword = encoder.encode(password);

    User user = new User();
    user.setUsername(username);
    user.setPassword(hashedPassword);

    User savedUser = repository.save(user);
    return ResponseEntity.ok(jwtUtil.generateToken(savedUser.getId()));
  }
}
