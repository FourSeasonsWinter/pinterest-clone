package com.luiz.backend.unit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.luiz.backend.dtos.PinDto;
import com.luiz.backend.dtos.PinPostRequest;
import com.luiz.backend.entity.Pin;
import com.luiz.backend.entity.User;
import com.luiz.backend.exception.PinNotFoundException;
import com.luiz.backend.exception.UnauthorizedException;
import com.luiz.backend.exception.UserNotFoundException;
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
  void findPinsByTag_Success() {
    // Setup
    String tag = "style";
    Pageable pageable = PageRequest.of(0, 10);

    Pin pin1 = new Pin();
    pin1.setId(UUID.randomUUID());
    pin1.setTitle("Pin 1");

    Pin pin2 = new Pin();
    pin2.setId(UUID.randomUUID());
    pin2.setTitle("Pin 2");

    List<Pin> pins = List.of(pin1, pin2);
    Page<Pin> pinPage = new PageImpl<>(pins, pageable, pins.size());

    PinDto pinDto1 = new PinDto();
    pinDto1.setId(pin1.getId());
    pinDto1.setTitle(pin1.getTitle());

    PinDto pinDto2 = new PinDto();
    pinDto2.setId(pin2.getId());
    pinDto2.setTitle(pin2.getTitle());

    // Mock
    when(repository.findByTag(tag, pageable)).thenReturn(pinPage);
    when(mapper.toDto(pin1)).thenReturn(pinDto1);
    when(mapper.toDto(pin2)).thenReturn(pinDto2);

    // Method
    Page<PinDto> result = service.getPinsByTag(tag, pageable);

    // Assertions
    assertEquals(2, result.getTotalElements());
    assertEquals(2, result.getContent().size());
    assertEquals("Pin 1", result.getContent().get(0).getTitle());
    assertEquals("Pin 2", result.getContent().get(1).getTitle());

    // Verifications
    verify(repository, times(1)).findByTag(tag, pageable);
    verify(mapper, times(1)).toDto(pin1);
    verify(mapper, times(1)).toDto(pin2);
  }

  @Test
  void getPinsByUser_Success() {
    User user = new User();
    user.setId(UUID.randomUUID());
    user.setUsername("Test User");

    Pageable pageable = PageRequest.of(0, 10);

    Pin pin = new Pin();
    pin.setId(UUID.randomUUID());
    pin.setTitle("Pin");

    Pin pin2 = new Pin();
    pin2.setId(UUID.randomUUID());
    pin2.setTitle("Pin 2");

    List<Pin> pins = List.of(pin, pin2);
    Page<Pin> pinPage = new PageImpl<>(pins, pageable, pins.size());

    PinDto pinDto = new PinDto();
    pinDto.setId(pin.getId());
    pinDto.setTitle(pin.getTitle());

    PinDto pinDto2 = new PinDto();
    pinDto2.setId(pin2.getId());
    pinDto2.setTitle(pin2.getTitle());

    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(repository.findByUser(user, pageable)).thenReturn(pinPage);
    when(mapper.toDto(pin)).thenReturn(pinDto);
    when(mapper.toDto(pin2)).thenReturn(pinDto2);

    Page<PinDto> result = service.getPinsByUser(user.getId(), pageable);

    assertEquals(2, result.getTotalElements());
    assertEquals(2, result.getContent().size());
    assertEquals("Pin", result.getContent().get(0).getTitle());
    assertEquals("Pin 2", result.getContent().get(1).getTitle());
    assertEquals(pin.getId(), result.getContent().get(0).getId());
    assertEquals(pin2.getId(), result.getContent().get(1).getId());

    verify(userRepository, times(1)).findById(user.getId());
    verify(repository, times(1)).findByUser(user, pageable);
    verify(mapper, times(1)).toDto(pin);
    verify(mapper, times(1)).toDto(pin2);
  }

  @Test
  void getPinsByUser_UserNotFound() {
    UUID userId = UUID.randomUUID();
    Pageable pageable = PageRequest.of(0, 10);

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> service.getPinsByUser(userId, pageable));

    verify(userRepository, times(1)).findById(userId);
    verify(repository, never()).findByUser(any(), any());
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
