package com.luiz.user_service.dtos;

import lombok.Data;

@Data
public class UserPasswordUpdateRequest {
  private String oldPassword;
  private String newPassword;
}
