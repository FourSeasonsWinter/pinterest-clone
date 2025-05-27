package com.luiz.backend.dtos;

import lombok.Value;

@Value
public class PinPostRequest {
  private String title;
  private String description;
  private String tag;
  private String imageUrl;
  private String sourceLink;
}
