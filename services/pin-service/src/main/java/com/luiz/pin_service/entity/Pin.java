package com.luiz.pin_service.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pins")
public class Pin {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String title;
  private String description;
  private String imageUrl;
  private String sourceLink;
  private LocalDateTime createdAt;

  private UUID userId;

  private Integer likesCount = 0;

  public void addLike() {
    this.likesCount++;
  }

  public void removeLike() {
    this.likesCount--;
  }
}
