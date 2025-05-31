package com.luiz.backend.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class PinBoardPostRequest {
  private UUID pinId;
  private UUID boardId;
}
