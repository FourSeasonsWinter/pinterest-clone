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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "boards")
public class Board {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;
  private String description;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private boolean isPrivate = false;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToMany(fetch = FetchType.LAZY, cascade = {
    CascadeType.PERSIST,
    CascadeType.MERGE
  })
  @JoinTable(
    name = "board_pins",
    joinColumns = @JoinColumn(name = "board_id"),
    inverseJoinColumns = @JoinColumn(name = "pin_id")
  )
  private Set<Pin> pins = new HashSet<>();

  public void addPin(Pin pin) {
    this.pins.add(pin);
    pin.getBoards().add(this);
  }

  public void removePin(Pin pin) {
    this.pins.remove(pin);
    pin.getBoards().remove(this);
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
