package com.luiz.backend.controllers;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luiz.backend.dtos.BoardDto;
import com.luiz.backend.dtos.PageDto;
import com.luiz.backend.dtos.PinBoardPostRequest;
import com.luiz.backend.dtos.PinDto;
import com.luiz.backend.entity.PinBoard;
import com.luiz.backend.entity.User;
import com.luiz.backend.services.PinBoardService;
import com.luiz.backend.util.GetUserUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pin-board")
@RequiredArgsConstructor
public class PinBoardController {

  private final PinBoardService service;
  private final GetUserUtil getUserUtil;
  
  @Operation(summary = "Get all pins on a board")
  @GetMapping("/boards/{boardId}")
  public ResponseEntity<PageDto<PinDto>> getPinsFromBoard(
    @PathVariable UUID boardId,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size
  ) {
    PageDto<PinDto> pins = service.getPinsFromBoard(boardId, PageRequest.of(page, size));
    return ResponseEntity.ok(pins);
  }

  @Operation(summary = "Get all boards that contains a pin")
  @GetMapping("/pins/{pinId}")
  public ResponseEntity<PageDto<BoardDto>> getBoardsFromPin(
    @PathVariable UUID pinId,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size
  ) {
    PageDto<BoardDto> boards = service.getBoardsFromPin(pinId, PageRequest.of(page, size));
    return ResponseEntity.ok(boards);
  }


  @Operation(summary = "Add a pin to a board")
  @SecurityRequirement(name = "bearerAuth")
  @PostMapping
  public ResponseEntity<PinBoard> addPinToBoard(
    @RequestBody PinBoardPostRequest request,
    Authentication authentication
  ) {
    User user = getUserUtil.getAuthenticatedUser();
    PinBoard pinBoard = service.addPinToBoard(request.getPinId(), request.getBoardId(), user);

    return ResponseEntity.ok(pinBoard);
  }

  @Operation(summary = "Remove a pin from a board")
  @SecurityRequirement(name = "bearerAuth")
  @DeleteMapping("/pinBoardId")
  public ResponseEntity<Void> removePinFromBoard(
    @PathVariable UUID pinBoardId,
    Authentication authentication
  ) {
    User user = getUserUtil.getAuthenticatedUser();
    service.removePinFromBoard(pinBoardId, user);

    return ResponseEntity.noContent().build();
  }
}
