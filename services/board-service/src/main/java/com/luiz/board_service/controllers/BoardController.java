package com.luiz.board_service.controllers;

import java.net.URI;
import java.util.List;
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
import com.luiz.board_service.entity.Board;
import com.luiz.board_service.mappers.BoardMapper;
import com.luiz.board_service.mappers.PageMapper;
import com.luiz.board_service.repository.BoardRepository;
import com.luiz.board_service.services.BoardService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

  private final BoardService service;
  private final BoardRepository repository;
  private final BoardMapper boardMapper;
  private final PageMapper pageMapper;

  @GetMapping("/{id}")
  public ResponseEntity<BoardDto> getBoardById(@PathVariable UUID id) {
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

  @PostMapping("/batch")
  public List<BoardDto> getBoardsByIds(@RequestBody List<UUID> boardIds) {
    List<Board> boards = repository.findAllById(boardIds);
    return boards.stream().map(boardMapper::toDto).toList();
  }
  

  @SecurityRequirement(name = "bearerAuth")
  @PostMapping
  public ResponseEntity<BoardDto> createBoard(
    @RequestBody BoardPostRequest request,
    @RequestHeader("X-User-Id") String userId,
    UriComponentsBuilder uriBuilder
  ) {
    BoardDto dto = service.createBoard(request, UUID.fromString(userId));
    URI uri = uriBuilder.path("/boards/{id}").buildAndExpand(dto.getId()).toUri();

    return ResponseEntity.created(uri).body(dto);
  }

  @SecurityRequirement(name = "bearerAuth")
  @PutMapping("/{id}")
  public ResponseEntity<BoardDto> updateBoard(
    @PathVariable UUID id,
    @RequestBody BoardUpdateRequest request,
    @RequestHeader("X-User-Id") String userId
  ) {
    BoardDto dto = service.updateBoard(id, request, UUID.fromString(userId));

    return ResponseEntity.ok(dto);
  }

  @SecurityRequirement(name = "bearerAuth")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBoard(
    @PathVariable UUID id,
    @RequestHeader("X-User-Id") String userId
  ) {
    service.deleteBoard(id, UUID.fromString(userId));

    return ResponseEntity.noContent().build();
  }
}
