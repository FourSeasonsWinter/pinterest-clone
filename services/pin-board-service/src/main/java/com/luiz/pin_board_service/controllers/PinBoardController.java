package com.luiz.pin_board_service.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luiz.pin_board_service.dtos.BoardDto;
import com.luiz.pin_board_service.dtos.PageDto;
import com.luiz.pin_board_service.dtos.PinBoardDto;
import com.luiz.pin_board_service.dtos.PinBoardPostRequest;
import com.luiz.pin_board_service.dtos.PinDto;
import com.luiz.pin_board_service.entity.PinBoard;
import com.luiz.pin_board_service.mappers.PageMapper;
import com.luiz.pin_board_service.mappers.PinBoardMapper;
import com.luiz.pin_board_service.services.PinBoardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pin-board")
@RequiredArgsConstructor
public class PinBoardController {

  private final PinBoardService service;
  private final PinBoardMapper mapper;
  private final PageMapper pageMapper;
  
  @Operation(summary = "Get all pins on a board")
  @GetMapping("/boards/{boardId}")
  public ResponseEntity<PageDto<PinDto>> getPinsOnBoard(
    @PathVariable UUID boardId,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size
  ) {
    Page<PinDto> pins = service.getPinsOnBoard(boardId, PageRequest.of(page, size));
    return ResponseEntity.ok(pageMapper.toPinDto(pins));
  }

  @Operation(summary = "Get all boards that contains a pin")
  @GetMapping("/pins/{pinId}")
  public ResponseEntity<PageDto<BoardDto>> getBoardsWithPin(
    @PathVariable UUID pinId,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size
  ) {
    Page<BoardDto> boards = service.getBoardsWithPin(pinId, PageRequest.of(page, size));
    return ResponseEntity.ok(pageMapper.toBoardDto(boards));
  }

  @Operation(summary = "Add a pin to a board")
  @SecurityRequirement(name = "bearerAuth")
  @PostMapping
  public ResponseEntity<PinBoardDto> addPinToBoard(
    @RequestBody PinBoardPostRequest request,
    @RequestHeader("X-User-Id") String userId
  ) {
    PinBoard pinBoard = service.addPinToBoard(request.getPinId(), request.getBoardId(), UUID.fromString(userId));
    return ResponseEntity.ok(mapper.toDto(pinBoard));
  }

  @Operation(summary = "Remove a pin from a board")
  @SecurityRequirement(name = "bearerAuth")
  @DeleteMapping
  public ResponseEntity<Void> removePinFromBoard(
    @RequestParam UUID boardId,
    @RequestParam UUID pinId,
    @RequestHeader("X-User-Id") String userId
  ) {
    service.removePinFromBoard(pinId, boardId, UUID.fromString(userId));
    return ResponseEntity.noContent().build();
  }
}
