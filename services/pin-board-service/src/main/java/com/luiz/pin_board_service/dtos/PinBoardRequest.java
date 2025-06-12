package com.luiz.pin_board_service.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class PinBoardRequest {
  private UUID pinId;
  private UUID boardId;
}
