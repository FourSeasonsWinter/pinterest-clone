package com.luiz.user_service.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class UserDto {
  private UUID id;
  private String username;
  private String email;
  private String bio;
  private String profilePictureUrl;
  private LocalDateTime createdAt;
}
