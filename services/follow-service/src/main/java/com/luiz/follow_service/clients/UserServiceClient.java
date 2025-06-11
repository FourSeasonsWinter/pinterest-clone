package com.luiz.follow_service.clients;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.luiz.follow_service.dtos.UserDto;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/users/{username}")
    UserDto getUserByUsername(@PathVariable String username);

    @GetMapping("/users/batch")
    List<UserDto> getUsersByIds(@RequestBody List<UUID> usersIds);
}
