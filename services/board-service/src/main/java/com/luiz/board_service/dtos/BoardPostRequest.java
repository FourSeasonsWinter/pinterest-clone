package com.luiz.board_service.dtos;

import lombok.Value;

@Value
public class BoardPostRequest {
  private String name;
  private String description;
}
