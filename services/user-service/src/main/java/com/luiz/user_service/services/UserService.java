package com.luiz.user_service.services;

import com.luiz.user_service.dtos.UserDto;
import com.luiz.user_service.dtos.UserPasswordUpdateRequest;
import com.luiz.user_service.dtos.UserUpdateRequest;
import com.luiz.user_service.entity.User;

public interface UserService {
  UserDto getCurrentUser(User user);
  UserDto getUser(String username);
  UserDto updateCurrentUser(UserUpdateRequest request, User user);
  void changePassword(UserPasswordUpdateRequest request, User user);
}
