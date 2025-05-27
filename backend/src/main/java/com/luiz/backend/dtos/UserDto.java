package com.luiz.backend.dtos;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.luiz.backend.entity.Board;
import com.luiz.backend.entity.Pin;

import lombok.Data;

@Data
public class UserDto {
  private UUID id;
  private String username;
  private String email;
  private String bio;
  private String profilePictureUrl;
  private LocalDateTime createdAt;
  private Set<Pin> pins;
  private Set<Board> boards;
}
