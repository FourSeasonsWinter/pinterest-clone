package com.luiz.pin_service.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.luiz.pin_service.dtos.PinDto;
import com.luiz.pin_service.dtos.PinPostRequest;
import com.luiz.pin_service.dtos.PinUpdateRequest;

public interface PinService {
  PinDto createPin(PinPostRequest request, UUID userId);
  void deletePin(UUID id, UUID userId);
  PinDto updatePin(UUID id, PinUpdateRequest request, UUID userId);
  PinDto getPin(UUID id);

  Page<PinDto> getPinsByUser(String username, Pageable pageable);
}
