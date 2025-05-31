package com.luiz.backend.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luiz.backend.dtos.BoardDto;
import com.luiz.backend.dtos.BoardPostRequest;
import com.luiz.backend.dtos.BoardUpdateRequest;
import com.luiz.backend.dtos.PageDto;
import com.luiz.backend.entity.User;
import com.luiz.backend.exception.UnauthenticatedException;
import com.luiz.backend.mappers.PageMapper;
import com.luiz.backend.repository.UserRepository;
import com.luiz.backend.services.BoardService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

  private final BoardService service;
  private final UserRepository userRepository;
  private final PageMapper pageMapper;

  @PostMapping
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<BoardDto> createBoard(
    @RequestBody BoardPostRequest request,
    Authentication authentication
  ) {
    User user = getAuthenticatedUser(authentication);
    BoardDto dto = service.createBoard(request, user);

    return ResponseEntity.ok(dto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<BoardDto> getBoard(@PathVariable UUID id) {
    BoardDto dto = service.getBoard(id);
    return ResponseEntity.ok(dto);
  }

  @GetMapping("/by-user/{username}")
  public ResponseEntity<PageDto<BoardDto>> getBoardsByUsername(
    @PathVariable String username,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size
  ) {
    Page<BoardDto> boards = service.getBoardsByUsername(username, PageRequest.of(page, size));
    return ResponseEntity.ok(pageMapper.toBoardDto(boards));
  }

  @PutMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<BoardDto> updateBoard(
    @PathVariable UUID id,
    @RequestBody BoardUpdateRequest request,
    Authentication authentication
  ) {
    User user = getAuthenticatedUser(authentication);
    BoardDto dto = service.updateBoard(id, request, user);

    return ResponseEntity.ok(dto);
  }

  @DeleteMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> deleteBoard(
    @PathVariable UUID id,
    Authentication authentication
  ) {
    User user = getAuthenticatedUser(authentication);
    service.deleteBoard(id, user);

    return ResponseEntity.noContent().build();
  }

  private User getAuthenticatedUser(Authentication authentication) {
    if (authentication == null) {
      throw new UnauthenticatedException("There is no logged in user");
    }

    String username = authentication.getName();
    User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username " + username));

    return user;
  }
}
