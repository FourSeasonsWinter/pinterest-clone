package com.luiz.pin_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.luiz.pin_service.dtos.UserDto;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/users/{username}")
    UserDto getUserByUsername(@PathVariable String username);
}
