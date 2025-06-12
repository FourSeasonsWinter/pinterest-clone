package com.luiz.like_service.clients;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.luiz.like_service.dtos.PinDto;

@FeignClient(name = "pin-service")
public interface PinServiceClient {
    
    @GetMapping("/pins/{id}")
    PinDto getPinById(@PathVariable UUID pinId);

    @PostMapping("/pins/batch")
    List<PinDto> getPinsByIds(@RequestBody List<UUID> pinsIds);
}
