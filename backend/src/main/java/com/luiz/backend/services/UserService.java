package com.luiz.backend.services;

import com.luiz.backend.dtos.UserDto;
import com.luiz.backend.dtos.UserPasswordUpdateRequest;
import com.luiz.backend.dtos.UserUpdateRequest;
import com.luiz.backend.entity.User;

public interface UserService {
  UserDto getCurrentUser(User user);
  UserDto getUser(String username);
  UserDto updateCurrentUser(UserUpdateRequest request, User user);
  void changePassword(UserPasswordUpdateRequest request, User user);
}
