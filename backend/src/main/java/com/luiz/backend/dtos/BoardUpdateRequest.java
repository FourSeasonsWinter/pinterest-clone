package com.luiz.backend.dtos;

import lombok.Value;

@Value
public class BoardUpdateRequest {
  private String name;
  private String description;
  private String isPrivate;
}
