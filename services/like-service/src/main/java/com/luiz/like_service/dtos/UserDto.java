package com.luiz.like_service.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class UserDto {
    private UUID id;
    private String username;
}
