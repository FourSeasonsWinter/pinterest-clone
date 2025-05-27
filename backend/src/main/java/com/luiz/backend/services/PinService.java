package com.luiz.backend.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.luiz.backend.dtos.PinDto;
import com.luiz.backend.dtos.PinPostRequest;
import com.luiz.backend.entity.User;

public interface PinService {
  PinDto createPin(PinPostRequest request, User user);
  PinDto getPin(UUID id);
  void deletePin(UUID id, User user);
  Page<PinDto> getPinsByTag(String tag, Pageable pageable);
  Page<PinDto> getPinsByUser(UUID userId, Pageable pageable);
}
