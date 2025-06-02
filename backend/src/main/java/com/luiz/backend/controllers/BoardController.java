package com.luiz.backend.controllers;

import java.net.URI;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.luiz.backend.dtos.BoardDto;
import com.luiz.backend.dtos.BoardPostRequest;
import com.luiz.backend.dtos.BoardUpdateRequest;
import com.luiz.backend.dtos.PageDto;
import com.luiz.backend.entity.User;
import com.luiz.backend.mappers.PageMapper;
import com.luiz.backend.services.BoardService;
import com.luiz.backend.util.GetUserUtil;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

  private final BoardService service;
  private final PageMapper pageMapper;
  private final GetUserUtil getUserUtil;

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
    User user = getUserUtil.getUserByUsername(username);
    Page<BoardDto> boards = service.getBoardsByUser(user, PageRequest.of(page, size));
    return ResponseEntity.ok(pageMapper.toBoardDto(boards));
  }

  @PostMapping
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<BoardDto> createBoard(
    @RequestBody BoardPostRequest request,
    Authentication authentication,
    UriComponentsBuilder uriBuilder
  ) {
    User user = getUserUtil.getAuthenticatedUser();
    BoardDto dto = service.createBoard(request, user);
    URI uri = uriBuilder.path("/boards/{id}").buildAndExpand(dto.getId()).toUri();

    return ResponseEntity.created(uri).body(dto);
  }

  @PutMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<BoardDto> updateBoard(
    @PathVariable UUID id,
    @RequestBody BoardUpdateRequest request,
    Authentication authentication
  ) {
    User user = getUserUtil.getAuthenticatedUser();
    BoardDto dto = service.updateBoard(id, request, user);

    return ResponseEntity.ok(dto);
  }

  @DeleteMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> deleteBoard(
    @PathVariable UUID id,
    Authentication authentication
  ) {
    User user = getUserUtil.getAuthenticatedUser();
    service.deleteBoard(id, user);

    return ResponseEntity.noContent().build();
  }
}
