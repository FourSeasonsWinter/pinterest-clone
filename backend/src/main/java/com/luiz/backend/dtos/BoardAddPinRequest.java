package com.luiz.backend.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class BoardAddPinRequest {
  private UUID pinId;
}
