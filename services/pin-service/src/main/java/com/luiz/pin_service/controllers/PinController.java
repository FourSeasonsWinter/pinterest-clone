package com.luiz.pin_service.controllers;

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

import com.luiz.pin_service.dtos.PageDto;
import com.luiz.pin_service.dtos.PinDto;
import com.luiz.pin_service.dtos.PinPostRequest;
import com.luiz.pin_service.dtos.PinUpdateRequest;
import com.luiz.pin_service.entity.Pin;
import com.luiz.pin_service.mappers.PageMapper;
import com.luiz.pin_service.mappers.PinMapper;
import com.luiz.pin_service.repository.PinRepository;
import com.luiz.pin_service.services.PinService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pins")
@RequiredArgsConstructor
public class PinController {
  
  private final PinService service;
  private final PinRepository repository;
  private final PinMapper pinMapper;
  private final PageMapper pageMapper;

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
    Page<PinDto> pinsPage = service.getPinsByUser(username, PageRequest.of(page, size));
    return ResponseEntity.ok(pageMapper.toDto(pinsPage));
  }

  @PostMapping("/batch")
  public List<PinDto> getPinsByIds(@RequestBody List<UUID> pinsIds) {
    List<Pin> pins = repository.findAllById(pinsIds);
    return pins.stream().map(pinMapper::toDto).toList();
  }
  

  @PostMapping
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<PinDto> createPin(
    @RequestBody PinPostRequest request,
    @RequestHeader("X-User-Id") String userId,
    UriComponentsBuilder uriBuilder
  ) {
    PinDto pin = service.createPin(request, UUID.fromString(userId));
    URI uri = uriBuilder.path("/pins/{id}").buildAndExpand(pin.getId()).toUri();

    return ResponseEntity.created(uri).body(pin);
  }

  @PutMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<PinDto> updatePin(
    @PathVariable UUID id,
    @RequestBody PinUpdateRequest request,
    @RequestHeader("X-User-Id") String userId
  ) {
    PinDto dto = service.updatePin(id, request, UUID.fromString(userId));

    return ResponseEntity.ok(dto);
  }

  @DeleteMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> deletePin(
    @PathVariable UUID id,
    @RequestHeader("X-User-Id") String userId
  ) {
    service.deletePin(id, UUID.fromString(userId));

    return ResponseEntity.noContent().build();
  }
}
