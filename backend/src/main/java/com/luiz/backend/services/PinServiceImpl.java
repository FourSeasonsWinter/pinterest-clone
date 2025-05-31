package com.luiz.backend.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.luiz.backend.dtos.PinDto;
import com.luiz.backend.dtos.PinPostRequest;
import com.luiz.backend.dtos.PinUpdateRequest;
import com.luiz.backend.entity.Pin;
import com.luiz.backend.entity.User;
import com.luiz.backend.exception.PinNotFoundException;
import com.luiz.backend.exception.UnauthorizedException;
import com.luiz.backend.mappers.PinMapper;
import com.luiz.backend.repository.PinRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PinServiceImpl implements PinService {

  private final PinRepository repository;
  private final PinMapper mapper;

  @Override
  public PinDto createPin(PinPostRequest request, User user) {
    if (request == null || user == null) {
      throw new IllegalArgumentException("Request or user cannot be null");
    }
    
    Pin pin = mapper.toEntity(request);
    pin.setUser(user);

    PinDto dto = mapper.toDto(repository.save(pin));
    dto.setUserId(user.getId());

    return dto;
  }

  @Override
  public PinDto updatePin(UUID id, PinUpdateRequest request, User user) {
    Pin pin = repository.findById(id).orElseThrow(() -> new PinNotFoundException("Pin not found with id " + id));

    pin.setTitle(request.getTitle());
    pin.setDescription(request.getDescription());
    repository.save(pin);

    return mapper.toDto(pin);
  }

  @Override
  public void deletePin(UUID id, User user) {
    // TODO Use a custom query to optimize data fetch
    Pin pin = repository.findById(id).orElseThrow(() -> new PinNotFoundException("Pin not found with id " + id));

    if (!pin.getUser().getId().equals(user.getId())) {
      throw new UnauthorizedException(
          "User with ID " + user.getId() + " is not authorized to delete pin with ID " + id);
    }

    repository.delete(pin);
  }

  @Override
  public PinDto getPin(UUID id) {
    Pin pin = repository.findById(id).orElseThrow(() -> new PinNotFoundException("Pin not found with id " + id));
    return mapper.toDto(pin);
  }

  @Override
  public Page<PinDto> getPinsByUser(User user, Pageable pageable) {
    Page<Pin> pinsPage = repository.findByUser(user, pageable);
    return pinsPage.map(mapper::toDto);
  }
}
