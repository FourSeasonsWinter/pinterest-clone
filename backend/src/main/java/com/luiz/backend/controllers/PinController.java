package com.luiz.backend.controllers;

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

import com.luiz.backend.dtos.PageDto;
import com.luiz.backend.dtos.PinDto;
import com.luiz.backend.dtos.PinPostRequest;
import com.luiz.backend.dtos.PinUpdateRequest;
import com.luiz.backend.entity.User;
import com.luiz.backend.mappers.PageMapper;
import com.luiz.backend.services.PinService;
import com.luiz.backend.util.GetUserUtil;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pins")
@RequiredArgsConstructor
public class PinController {
  
  private final PinService service;
  private final PageMapper pageMapper;
  private final GetUserUtil getUserUtil;

  @GetMapping("/{id}")
  public ResponseEntity<PinDto> getPin(@PathVariable UUID id) {
    PinDto pin = service.getPin(id);
    return ResponseEntity.ok(pin);
  }

  @GetMapping("/by-user")
  public ResponseEntity<PageDto<PinDto>> getPinsByUser(
    @RequestParam String username,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size
  ) {
    User user = getUserUtil.getUser(username);
    Page<PinDto> pinsPage = service.getPinsByUser(user, PageRequest.of(page, size));
    return ResponseEntity.ok(pageMapper.toPinDto(pinsPage));
  }

  @PostMapping
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<PinDto> createPin(
    @RequestBody PinPostRequest request,
    Authentication authentication
  ) {
    User user = getUserUtil.getAuthenticatedUser();
    return ResponseEntity.ok(service.createPin(request, user));
  }

  @PutMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<PinDto> updatePin(
    @PathVariable UUID id,
    @RequestBody PinUpdateRequest request,
    Authentication authentication
  ) {
    User user = getUserUtil.getAuthenticatedUser();
    PinDto dto = service.updatePin(id, request, user);

    return ResponseEntity.ok(dto);
  }

  @DeleteMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> deletePin(
    @PathVariable UUID id,
    Authentication authentication
  ) {
    User user = getUserUtil.getAuthenticatedUser();
    service.deletePin(id, user);

    return ResponseEntity.noContent().build();
  }
}
