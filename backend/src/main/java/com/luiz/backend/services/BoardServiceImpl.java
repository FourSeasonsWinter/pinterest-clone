package com.luiz.backend.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luiz.backend.dtos.BoardDto;
import com.luiz.backend.dtos.BoardPostRequest;
import com.luiz.backend.dtos.BoardUpdateRequest;
import com.luiz.backend.entity.Board;
import com.luiz.backend.entity.User;
import com.luiz.backend.exception.BoardNotFoundException;
import com.luiz.backend.exception.UnauthorizedException;
import com.luiz.backend.mappers.BoardMapper;
import com.luiz.backend.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

  private final BoardRepository repository;
  private final BoardMapper mapper;

  @Override
  @Transactional
  public BoardDto createBoard(BoardPostRequest request, User user) {
    if (request == null || user == null) {
      throw new IllegalArgumentException("Request or user cannot be null");
    }

    Board board = mapper.toEntity(request);
    board.setUser(user);

    BoardDto boardDto = mapper.toDto(repository.save(board));
    boardDto.setUserId(user.getId());

    return boardDto;
  }

  @Override
  @Transactional
  public BoardDto updateBoard(UUID boardId, BoardUpdateRequest request, User user) {
    Board board = getBoardIfAuthenticated(boardId, user);

    mapper.update(request, board);
    repository.save(board);

    return mapper.toDto(board);
  }

  @Override
  public void deleteBoard(UUID boardId, User user) {
    Board board = getBoardIfAuthenticated(boardId, user);
    repository.delete(board);
  }

  @Override
  public BoardDto getBoard(UUID boardId) {
    Board board = repository.findById(boardId).orElseThrow(() -> new BoardNotFoundException("Board not found with id " + boardId));
    return mapper.toDto(board);
  }

  @Override
  public Page<BoardDto> getBoardsByUser(User user, Pageable pageable) {
    Page<Board> boardsPage = repository.findByUser(user, pageable);
    return boardsPage.map(mapper::toDto);
  }

  private Board getBoardIfAuthenticated(UUID boardId, User user) {
    Board board = repository.findById(boardId).orElseThrow(() -> new BoardNotFoundException("Board not found with id " + boardId));
    if (!board.getUser().getId().equals(user.getId())) {
      throw new UnauthorizedException("Current user is not the owner of this board");
    }
    return board;
  }
}
