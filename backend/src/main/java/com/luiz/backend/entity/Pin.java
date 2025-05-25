package com.luiz.backend.entity;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
public class Pin {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private UUID id;
  private String title;
  private String description;
  private URL image_url;
  private URI link;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(mappedBy = "pin")
  private List<Comment> comments;

  @OneToMany(mappedBy = "pin")
  private List<BoardPin> boardPins;
}
