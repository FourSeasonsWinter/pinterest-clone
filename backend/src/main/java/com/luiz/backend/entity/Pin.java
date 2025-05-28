package com.luiz.backend.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
  private String tags;
  private String imageUrl;
  private String sourceLink;
  private LocalDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToMany(mappedBy = "pins", fetch = FetchType.LAZY)
  private Set<Board> boards = new HashSet<>();

  public void addBoard(Board board) {
    this.boards.add(board);
    board.getPins().add(this);
  }
  
  public void removeBoard(Board board) {
    this.boards.remove(board);
    board.getPins().remove(this);
  }
}
