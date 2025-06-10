package com.luiz.pin_service.dtos;

import lombok.Data;

@Data
public class PinUpdateRequest {
  private String title;
  private String description;
}
