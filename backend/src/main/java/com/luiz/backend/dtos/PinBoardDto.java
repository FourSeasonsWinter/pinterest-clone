package com.luiz.backend.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class PinBoardDto {
  private UUID id;
  private UUID pinId;
  private UUID boardId;
}
