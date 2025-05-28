package com.luiz.backend.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luiz.backend.dtos.BoardDto;
import com.luiz.backend.dtos.BoardPostRequest;
import com.luiz.backend.dtos.BoardUpdateRequest;
import com.luiz.backend.dtos.PinDto;
import com.luiz.backend.entity.Board;
import com.luiz.backend.entity.Pin;
import com.luiz.backend.entity.User;
import com.luiz.backend.exception.BoardNotFoundException;
import com.luiz.backend.exception.PinNotFoundException;
import com.luiz.backend.exception.UnauthenticatedException;
import com.luiz.backend.exception.UserNotFoundException;
import com.luiz.backend.mappers.BoardMapper;
import com.luiz.backend.mappers.PinMapper;
import com.luiz.backend.repository.BoardRepository;
import com.luiz.backend.repository.PinRepository;
import com.luiz.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

  private final BoardRepository repository;
  private final BoardMapper mapper;
  private final UserRepository userRepository;
  private final PinMapper pinMapper;
  private final PinRepository pinRepository;

  @Override
  @Transactional
  public BoardDto createBoard(BoardPostRequest request, User user) {
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
  @Transactional
  public BoardDto addPin(UUID boardId, UUID pinId, User user) {
    Board board = getBoardIfAuthenticated(boardId, user);
    Pin pin = pinRepository.findById(pinId).orElseThrow(() -> new PinNotFoundException("Pin not found with id " + pinId));

    pin.setUser(user);
    board.addPin(pin);

    BoardDto savedBoard = mapper.toDto(repository.save(board));

    return savedBoard;
  }

  @Override
  @Transactional
  public BoardDto removePin(UUID boardId, UUID pinId, User user) {
    Board board = getBoardIfAuthenticated(boardId, user);
    Pin pin = pinRepository.findById(pinId).orElseThrow(() -> new PinNotFoundException("Pin not found with id " + pinId));

    board.removePin(pin);
    Board savedBoard = repository.save(board);

    return mapper.toDto(savedBoard);
  }

  @Override
  public BoardDto getBoard(UUID boardId) {
    Board board = repository.findById(boardId).orElseThrow(() -> new BoardNotFoundException("Board not found with id " + boardId));
    return mapper.toDto(board);
  }

  @Override
  public Page<PinDto> getPinsOnBoard(UUID boardId, Pageable pageable) {
    Page<Pin> pinPage = repository.findPinsByBoardId(boardId, pageable);
    return pinPage.map(pinMapper::toDto);
  }

  @Override
  public Page<BoardDto> getBoardsFromUser(UUID userId, Pageable pageable) {
    User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));
    Page<Board> boardsPage = repository.findByUser(user, pageable);

    return boardsPage.map(mapper::toDto);
  }

  @Override
  public Page<BoardDto> getAllBoards(Pageable pageable) {
    Page<Board> boardsPage = repository.findAll(pageable);
    return boardsPage.map(mapper::toDto);
  }

  private Board getBoardIfAuthenticated(UUID boardId, User user) {
    Board board = repository.findById(boardId).orElseThrow(() -> new BoardNotFoundException("Board not found with id " + boardId));
    if (!board.getUser().getId().equals(user.getId())) {
      throw new UnauthenticatedException("Current user is not the owner of this board");
    }
    return board;
  }

  @Override
  public Page<BoardDto> getBoardsByUsername(String username, Pageable pageable) {
    Page<Board> boardPage = repository.findByUser_Username(username, pageable);
    return boardPage.map(mapper::toDto);
  }
}
