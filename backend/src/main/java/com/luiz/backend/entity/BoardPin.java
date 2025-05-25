package com.luiz.backend.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class BoardPin {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "board_id", nullable = false)
  private Board board;

  @ManyToOne
  @JoinColumn(name = "pin_id", nullable = false)
  private Pin pin;
}
