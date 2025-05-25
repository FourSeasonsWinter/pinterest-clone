package com.luiz.backend.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String username;
  private String email;
  private String password;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Pin> pins = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Like> likes = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  private List<Board> boards;

  @OneToMany(mappedBy = "user")
  private List<Comment> comments;
}
