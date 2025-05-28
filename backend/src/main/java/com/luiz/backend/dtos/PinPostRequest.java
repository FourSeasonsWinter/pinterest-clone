package com.luiz.backend.dtos;

import lombok.Data;

@Data
public class PinPostRequest {
  private String title;
  private String description;
  private String tags;
  private String imageUrl;
  private String sourceLink;
}
