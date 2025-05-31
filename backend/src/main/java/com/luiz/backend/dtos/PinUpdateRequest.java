package com.luiz.backend.dtos;

import lombok.Data;

@Data
public class PinUpdateRequest {
  private String title;
  private String description;
}
