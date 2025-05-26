package com.luiz.backend.dtos;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
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
  private URL profilePictureUrl;
  private LocalDateTime createdAt;
  private List<Pin> pins;
  private List<Board> boards;
}
