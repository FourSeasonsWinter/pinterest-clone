package com.luiz.backend.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Follow {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "follower_id", referencedColumnName = "id", nullable = false)
  private User follower;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "followed_by_id", referencedColumnName = "id", nullable = false)
  private User followedBy;
}
