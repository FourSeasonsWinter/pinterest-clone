package com.luiz.user_service.dtos;

import lombok.Data;

@Data
public class UserUpdateRequest {
  private String username;
  private String email;
  private String bio;
}
