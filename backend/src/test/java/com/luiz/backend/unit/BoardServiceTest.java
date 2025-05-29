package com.luiz.backend.unit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
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

import com.luiz.backend.dtos.BoardDto;
import com.luiz.backend.dtos.BoardPostRequest;
import com.luiz.backend.dtos.BoardUpdateRequest;
import com.luiz.backend.entity.Board;
import com.luiz.backend.entity.User;
import com.luiz.backend.exception.UnauthorizedException;
import com.luiz.backend.mappers.BoardMapper;
import com.luiz.backend.repository.BoardRepository;
import com.luiz.backend.services.BoardServiceImpl;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

  @InjectMocks
  private BoardServiceImpl service;

  @Mock
  private BoardRepository repository;

  @Mock
  private BoardMapper mapper;

  @Test
  void createBoard_Success() {
    User user = new User();
    user.setId(UUID.randomUUID());
    user.setUsername("Test User");

    BoardPostRequest request = new BoardPostRequest("Test Board", user.getId());

    Board board = new Board();
    board.setId(UUID.randomUUID());
    board.setName("Test Board");
    board.setUser(user);

    BoardDto boardDto = new BoardDto();
    boardDto.setId(board.getId());
    boardDto.setName(board.getName());
    boardDto.setUserId(board.getUser().getId());
    boardDto.setUsername(user.getUsername());

    when(mapper.toEntity(request)).thenReturn(board);
    when(mapper.toDto(board)).thenReturn(boardDto);
    when(repository.save(board)).thenReturn(board);

    BoardDto result = service.createBoard(request, user);

    assertEquals(board.getId(), result.getId());
    assertEquals("Test Board", result.getName());
    assertEquals(user.getId(), result.getUserId());
    assertEquals(user.getUsername(), result.getUsername());

    verify(repository, times(1)).save(board);
    verify(mapper, times(1)).toEntity(request);
    verify(mapper, times(1)).toDto(board);
  }

  @Test
  void updateBoard_Success() {
    UUID boardId = UUID.randomUUID();

    User user = new User();
    user.setId(UUID.randomUUID());
    user.setUsername("Test User");

    Board board = new Board();
    board.setId(boardId);
    board.setUser(user);
    board.setName("Board");
    board.setDescription("Board");

    BoardUpdateRequest request = new BoardUpdateRequest();
    request.setName("Updated Board");
    request.setDescription("Updated Description");
    request.setPrivate(true);

    doAnswer(invocation -> {
      BoardUpdateRequest req = invocation.getArgument(0);
      Board b = invocation.getArgument(1);
      b.setName(req.getName());
      b.setDescription(req.getDescription());
      b.setPrivate(req.isPrivate());
      return null;
    }).when(mapper).update(any(BoardUpdateRequest.class), any(Board.class));

    when(repository.findById(boardId)).thenReturn(Optional.of(board));
    when(repository.save(board)).thenReturn(board);
    when(mapper.toDto(board)).thenReturn(new BoardDto());

    assertDoesNotThrow(() -> service.updateBoard(boardId, request, user));

    assertEquals("Updated Board", board.getName());
    assertEquals("Updated Description", board.getDescription());
    assertTrue(board.isPrivate());

    verify(repository, times(1)).findById(boardId);
    verify(repository, times(1)).save(board);
    verify(mapper, times(1)).update(request, board);
    verify(mapper, times(1)).toDto(board);
  }

  @Test
  void deleteBoard_Success() {
    User user = new User();
    user.setId(UUID.randomUUID());

    UUID boardId = UUID.randomUUID();
    Board board = new Board();
    board.setId(boardId);
    board.setUser(user);

    when(repository.findById(boardId)).thenReturn(Optional.of(board));

    assertDoesNotThrow(() -> service.deleteBoard(boardId, user));
    verify(repository, times(1)).findById(boardId);
    verify(repository, times(1)).delete(board);
  }

  @Test
  void updateBoard_Unauthorized() {
    UUID boardId = UUID.randomUUID();

    User user = new User();
    user.setId(UUID.randomUUID());

    User unauthorizedUser = new User();
    user.setId(UUID.randomUUID());

    Board board = new Board();
    board.setUser(user);

    when(repository.findById(boardId)).thenReturn(Optional.of(board));

    assertThrows(UnauthorizedException.class,
        () -> service.updateBoard(boardId, new BoardUpdateRequest(), unauthorizedUser));

    verify(repository, times(1)).findById(boardId);
    verify(repository, never()).save(any());
    verify(mapper, never()).toDto(any());
  }

  @Test
  void createBoard_NullRequestOrUser_ThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> service.createBoard(null, new User()));
    assertThrows(IllegalArgumentException.class,
        () -> service.createBoard(new BoardPostRequest("Test Board", UUID.randomUUID()), null));
  }
}
