package com.luiz.board_service.controllers;

import java.net.URI;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.luiz.board_service.dtos.BoardDto;
import com.luiz.board_service.dtos.BoardPostRequest;
import com.luiz.board_service.dtos.BoardUpdateRequest;
import com.luiz.board_service.dtos.PageDto;
import com.luiz.board_service.mappers.PageMapper;
import com.luiz.board_service.services.BoardService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

  private final BoardService service;
  private final PageMapper pageMapper;

  @GetMapping("/{id}")
  public ResponseEntity<BoardDto> getBoard(@PathVariable UUID id) {
    BoardDto dto = service.getBoard(id);
    return ResponseEntity.ok(dto);
  }

  @GetMapping("/by-user")
  public ResponseEntity<PageDto<BoardDto>> getBoardsByUsername(
    @RequestParam String username,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size
  ) {
    Page<BoardDto> boards = service.getBoardsByUsername(username, PageRequest.of(page, size));
    return ResponseEntity.ok(pageMapper.toDto(boards));
  }

  @PostMapping
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<BoardDto> createBoard(
    @RequestBody BoardPostRequest request,
    @RequestHeader("X-User-Id") String userId,
    UriComponentsBuilder uriBuilder
  ) {
    BoardDto dto = service.createBoard(request, UUID.fromString(userId));
    URI uri = uriBuilder.path("/boards/{id}").buildAndExpand(dto.getId()).toUri();

    return ResponseEntity.created(uri).body(dto);
  }

  @PutMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<BoardDto> updateBoard(
    @PathVariable UUID id,
    @RequestBody BoardUpdateRequest request,
    @RequestHeader("X-User-Id") String userId
  ) {
    BoardDto dto = service.updateBoard(id, request, UUID.fromString(userId));

    return ResponseEntity.ok(dto);
  }

  @DeleteMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> deleteBoard(
    @PathVariable UUID id,
    @RequestHeader("X-User-Id") String userId
  ) {
    service.deleteBoard(id, UUID.fromString(userId));

    return ResponseEntity.noContent().build();
  }
}
