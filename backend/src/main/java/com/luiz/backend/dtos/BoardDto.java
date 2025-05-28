package com.luiz.backend.dtos;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import lombok.Data;

@Data
public class BoardDto {
  private UUID id;
  private String name;
  private String description;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private boolean isPrivate;
  private UUID userId;
  private String username;
  private Set<PinDto> pins;
}
