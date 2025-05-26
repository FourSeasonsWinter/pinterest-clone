package com.luiz.backend.dtos;

import lombok.Data;

@Data
public class UserPasswordUpdateRequest {
  private String oldPassword;
  private String newPassword;
}
