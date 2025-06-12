package com.luiz.board_service.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luiz.board_service.clients.UserServiceClient;
import com.luiz.board_service.dtos.BoardDto;
import com.luiz.board_service.dtos.BoardPostRequest;
import com.luiz.board_service.dtos.BoardUpdateRequest;
import com.luiz.board_service.dtos.UserDto;
import com.luiz.board_service.entity.Board;
import com.luiz.board_service.exceptions.BoardNotFoundException;
import com.luiz.board_service.exceptions.UnauthorizedException;
import com.luiz.board_service.mappers.BoardMapper;
import com.luiz.board_service.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

  private final BoardRepository repository;
  private final BoardMapper mapper;
  private final UserServiceClient userServiceClient;

  @Override
  @Transactional
  public BoardDto createBoard(BoardPostRequest request, UUID userId) {
    Board board = mapper.toEntity(request);
    board.setUserId(userId);

    BoardDto boardDto = mapper.toDto(repository.save(board));

    return boardDto;
  }

  @Override
  @Transactional
  public BoardDto updateBoard(UUID boardId, BoardUpdateRequest request, UUID userId) {
    Board board = getBoardIfAuthenticated(boardId, userId);

    mapper.update(request, board);
    repository.save(board);

    return mapper.toDto(board);
  }

  @Override
  public void deleteBoard(UUID boardId, UUID userId) {
    Board board = getBoardIfAuthenticated(boardId, userId);
    repository.delete(board);
  }

  @Override
  public BoardDto getBoard(UUID boardId) {
    Board board = repository.findById(boardId).orElseThrow(() -> new BoardNotFoundException("Board not found with id " + boardId));
    return mapper.toDto(board);
  }

  @Override
  public Page<BoardDto> getBoardsByUserId(UUID userId, Pageable pageable) {
    Page<Board> boardsPage = repository.findByUserId(userId, pageable);
    return boardsPage.map(mapper::toDto);
  }

  @Override
  public Page<BoardDto> getBoardsByUsername(String username, Pageable pageable) {
    UserDto user = userServiceClient.getUserByUsername(username);
    Page<Board> boardsPage = repository.findByUserId(user.getId(), pageable);
    return boardsPage.map(mapper::toDto);
  }
  
  private Board getBoardIfAuthenticated(UUID boardId, UUID userId) {
    Board board = repository.findById(boardId).orElseThrow(() -> new BoardNotFoundException("Board not found with id " + boardId));
    if (!board.getUserId().equals(userId)) {
      throw new UnauthorizedException("Current user is not the owner of this board");
    }
    return board;
  }
}
