package com.luiz.user_service.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.luiz.user_service.dtos.UserDto;
import com.luiz.user_service.dtos.UserPasswordUpdateRequest;
import com.luiz.user_service.dtos.UserUpdateRequest;
import com.luiz.user_service.entity.User;
import com.luiz.user_service.exceptions.UserNotFoundException;
import com.luiz.user_service.mapper.UserMapper;
import com.luiz.user_service.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository repository;
  private final UserMapper mapper;
  private final PasswordEncoder encoder;

  @Override
  @Transactional
  public UserDto getCurrentUser(User user) {
    return mapper.toDto(user);
  }

  @Override
  @Transactional
  public UserDto getUser(String username) {
    User user = repository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found with username " + username));
    return mapper.toDto(user);
  }

  @Override
  public UserDto updateCurrentUser(UserUpdateRequest request, User user) {
    mapper.update(request, user);
    repository.save(user);

    return mapper.toDto(user);
  }

  @Override
  public void changePassword(UserPasswordUpdateRequest request, User user) {
    String hashedNewPassword = encoder.encode(request.getNewPassword());

    user.setPassword(hashedNewPassword);
    repository.save(user);
  }
}
