package com.luiz.pin_service.dtos;

import lombok.Data;

@Data
public class PinPostRequest {
  private String title;
  private String description;
  private String imageUrl;
  private String sourceLink;
}
