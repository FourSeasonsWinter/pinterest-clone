package com.luiz.backend.dtos;

import java.util.UUID;

import lombok.Value;

@Value
public class BoardPostRequest {
  private String name;
  private UUID userId;
}
