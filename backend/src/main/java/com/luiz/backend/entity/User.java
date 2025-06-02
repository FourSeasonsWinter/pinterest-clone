package com.luiz.backend.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode(exclude = {"pins", "boards", "following", "followers"})
@ToString(exclude = {"pins", "boards", "following", "followers"})
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String username;
  private String email;
  private String password;
  private String bio;
  private String profilePictureUrl;
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Pin> pins = new HashSet<>();

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Board> boards = new HashSet<>();

  @OneToMany(mappedBy = "follower", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Follow> following = new HashSet<>();

  @OneToMany(mappedBy = "followedBy", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Follow> followers = new HashSet<>();
}
