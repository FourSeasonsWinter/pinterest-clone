package com.luiz.backend.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class FollowDto {
  private UUID followerId;
  private UUID followedById;
}
