package com.luiz.backend.unit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.luiz.backend.dtos.PinDto;
import com.luiz.backend.dtos.PinPostRequest;
import com.luiz.backend.entity.Pin;
import com.luiz.backend.entity.User;
import com.luiz.backend.exception.PinNotFoundException;
import com.luiz.backend.exception.UnauthorizedException;
import com.luiz.backend.mappers.PinMapper;
import com.luiz.backend.repository.PinRepository;
import com.luiz.backend.repository.UserRepository;
import com.luiz.backend.services.PinServiceImpl;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PinServiceTest {

  @InjectMocks
  private PinServiceImpl service;

  @Mock
  private PinRepository repository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PinMapper mapper;

  @Test
  void getPin_Success() {
    // Setup
    UUID pinId = UUID.randomUUID();
    Pin pin = new Pin();
    pin.setId(pinId);
    PinDto pinDto = new PinDto();
    pinDto.setId(pinId);

    // Mock behavior
    when(repository.findById(pinId)).thenReturn(Optional.of(pin));
    when(mapper.toDto(pin)).thenReturn(pinDto);

    // Method under test
    PinDto result = service.getPin(pinId);

    // Assertions
    assertEquals(pinId, result.getId());

    // Verify Mock interactions
    verify(repository, times(1)).findById(pinId);
  }

  @Test
  void createPin_Success() {
    User user = new User();
    user.setId(UUID.randomUUID());
    user.setUsername("Test User");

    Pin pin = new Pin();
    pin.setTitle("Test Pin");
    pin.setUser(user);

    PinDto pinDto = new PinDto();
    pinDto.setTitle("Test Pin");
    pinDto.setUserId(user.getId());

    PinPostRequest request = new PinPostRequest();
    request.setTitle("Test Pin");

    when(mapper.toEntity(request)).thenReturn(pin);
    when(repository.save(pin)).thenReturn(pin);
    when(mapper.toDto(pin)).thenReturn(pinDto);

    PinDto result = service.createPin(request, user);

    assertEquals("Test Pin", result.getTitle());
    assertEquals(user.getId(), result.getUserId());

    verify(repository, times(1)).save(pin);
    verify(mapper, times(1)).toEntity(request);
    verify(mapper, times(1)).toDto(pin);
  }

  @Test
  void deletePin_Success() {
    User user = new User();
    user.setId(UUID.randomUUID());
    user.setUsername("Test User");

    UUID pinId = UUID.randomUUID();
    Pin pin = new Pin();
    pin.setId(pinId);
    pin.setTitle("Test Pin");
    pin.setUser(user);

    when(repository.findById(pinId)).thenReturn(Optional.of(pin));

    assertDoesNotThrow(() -> service.deletePin(pinId, user));
    verify(repository, times(1)).findById(pinId);
    verify(repository, times(1)).delete(pin);
  }

  @Test
  void deletePin_NotFound() {
    UUID pinId = UUID.randomUUID();

    when(repository.findById(pinId)).thenReturn(Optional.empty());

    assertThrows(PinNotFoundException.class, () -> service.deletePin(pinId, new User()));
    verify(repository, times(1)).findById(pinId);
    verify(repository, never()).delete(any());
  }

  @Test
  void deletePin_Unauthorized() {
    User user = new User();
    user.setId(UUID.randomUUID());

    User otherUser = new User();
    otherUser.setId(UUID.randomUUID());

    UUID pinId = UUID.randomUUID();
    Pin pin = new Pin();
    pin.setId(pinId);
    pin.setUser(user);

    when(repository.findById(pinId)).thenReturn(Optional.of(pin));

    assertThrows(UnauthorizedException.class, () -> service.deletePin(pinId, otherUser));
    verify(repository, times(1)).findById(pinId);
    verify(repository, never()).delete(any());
  }

  @Test
  void getPin_NotFound() {
    UUID pinId = UUID.randomUUID();

    when(repository.findById(pinId)).thenReturn(Optional.empty());

    assertThrows(PinNotFoundException.class, () -> service.getPin(pinId));
    verify(repository, times(1)).findById(pinId);
  }
}
