package com.luiz.pin_service.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.luiz.pin_service.clients.UserServiceClient;
import com.luiz.pin_service.dtos.PinDto;
import com.luiz.pin_service.dtos.PinPostRequest;
import com.luiz.pin_service.dtos.PinUpdateRequest;
import com.luiz.pin_service.entity.Pin;
import com.luiz.pin_service.exceptions.PinNotFoundException;
import com.luiz.pin_service.exceptions.UnauthorizedException;
import com.luiz.pin_service.mappers.PinMapper;
import com.luiz.pin_service.repository.PinRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PinServiceImpl implements PinService {

  private final PinRepository repository;
  private final PinMapper mapper;
  private final UserServiceClient userServiceClient;

  @Override
  public PinDto createPin(PinPostRequest request, UUID userId) {
    if (request == null || userId == null) {
      throw new IllegalArgumentException("Request or user cannot be null");
    }
    
    Pin pin = mapper.toEntity(request);
    pin.setUserId(userId);

    return mapper.toDto(repository.save(pin));
  }

  @Override
  public PinDto updatePin(UUID id, PinUpdateRequest request, UUID userId) {
    Pin pin = repository.findById(id).orElseThrow(() -> new PinNotFoundException("Pin not found with id " + id));

    if (!pin.getUserId().equals(userId)) {
      throw new UnauthorizedException("User is not the owner of the pin");
    }

    pin.setTitle(request.getTitle());
    pin.setDescription(request.getDescription());
    repository.save(pin);

    return mapper.toDto(pin);
  }

  @Override
  public void deletePin(UUID id, UUID userId) {
    // TODO Use a custom query to optimize data fetch
    Pin pin = repository.findById(id).orElseThrow(() -> new PinNotFoundException("Pin not found with id " + id));

    if (!pin.getUserId().equals(userId)) {
      throw new UnauthorizedException(
          "User with ID " + userId + " is not authorized to delete pin with ID " + id);
    }

    repository.delete(pin);
  }

  @Override
  public PinDto getPin(UUID id) {
    Pin pin = repository.findById(id).orElseThrow(() -> new PinNotFoundException("Pin not found with id " + id));
    return mapper.toDto(pin);
  }

  @Override
  public Page<PinDto> getPinsByUser(String username, Pageable pageable) {
    UUID userId = userServiceClient.getUserByUsername(username).getId();
    Page<Pin> pinsPage = repository.findByUserId(userId, pageable);
    return pinsPage.map(mapper::toDto);
  }
}
