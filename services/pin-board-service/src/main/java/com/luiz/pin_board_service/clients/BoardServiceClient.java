package com.luiz.pin_board_service.clients;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.luiz.pin_board_service.dtos.BoardDto;

@FeignClient(name = "board-service")
public interface BoardServiceClient {
    
    @GetMapping("/boards/{id}")
    BoardDto getBoardById(@PathVariable UUID id);

    @PostMapping("/boards/batch")
    List<BoardDto> getBoardsByIds(@RequestBody List<UUID> boardIds);
}
