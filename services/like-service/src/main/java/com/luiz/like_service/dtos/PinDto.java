package com.luiz.like_service.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class PinDto {
  private UUID id;
  private String title;
  private String description;
  private String imageUrl;
  private String sourceLink;
  private LocalDateTime createdAt;
  private UUID userId;
}
