package com.luiz.like_service.clients;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.luiz.like_service.dtos.UserDto;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/users/{username}")
    UserDto getUserByUsername(@PathVariable String username);

    @PostMapping("/users/batch")
    List<UserDto> getUsersByIds(@RequestBody List<UUID> usersIds);
}
