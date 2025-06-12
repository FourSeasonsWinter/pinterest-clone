package com.luiz.board_service.dtos;

import lombok.Data;

@Data
public class BoardUpdateRequest {
  private String name;
  private String description;
  private boolean isPrivate;
}
