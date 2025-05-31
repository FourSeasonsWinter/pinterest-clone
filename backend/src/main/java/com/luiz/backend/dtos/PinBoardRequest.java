package com.luiz.backend.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class PinBoardRequest {
  private UUID pinId;
  private UUID boardId;
}
