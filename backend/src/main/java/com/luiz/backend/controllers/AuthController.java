package com.luiz.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luiz.backend.dtos.LoginRequest;
import com.luiz.backend.dtos.RegisterRequest;
import com.luiz.backend.entity.User;
import com.luiz.backend.exception.InvalidCredentialsException;
import com.luiz.backend.repository.UserRepository;
import com.luiz.backend.util.JwtUtil;

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

    try {
      authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(username, request.getPassword())
      );
      return jwtUtil.generateToken(username);
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

    repository.save(user);
    return ResponseEntity.ok(jwtUtil.generateToken(username));
  }
}
