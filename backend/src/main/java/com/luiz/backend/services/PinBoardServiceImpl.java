package com.luiz.backend.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.luiz.backend.dtos.BoardDto;
import com.luiz.backend.dtos.PinDto;
import com.luiz.backend.entity.Board;
import com.luiz.backend.entity.Pin;
import com.luiz.backend.entity.PinBoard;
import com.luiz.backend.entity.User;
import com.luiz.backend.exception.BoardNotFoundException;
import com.luiz.backend.exception.PinBoardNotFoundException;
import com.luiz.backend.exception.PinNotFoundException;
import com.luiz.backend.exception.UnauthorizedException;
import com.luiz.backend.mappers.BoardMapper;
import com.luiz.backend.mappers.PinMapper;
import com.luiz.backend.repository.BoardRepository;
import com.luiz.backend.repository.PinBoardRepository;
import com.luiz.backend.repository.PinRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PinBoardServiceImpl implements PinBoardService {

  private final PinBoardRepository repository;
  private final PinRepository pinRepository;
  private final BoardRepository boardRepository;
  private final PinMapper pinMapper;
  private final BoardMapper boardMapper;

  @Override
  public Page<PinDto> getPinsFromBoard(UUID boardId, Pageable pageable) {
    Page<Pin> pins = repository.findPinsByBoardId(boardId, pageable);
    return pins.map(pinMapper::toDto);
  }

  @Override
  public Page<BoardDto> getBoardsFromPin(UUID pinId, Pageable pageable) {
    Page<Board> boards = repository.findBoardsByPinId(pinId, pageable);
    return boards.map(boardMapper::toDto);
  }

  @Override
  public PinBoard addPinToBoard(UUID pinId, UUID boardId, User user) {
    Pin pin = pinRepository.findById(pinId).orElseThrow(() -> new PinNotFoundException("Pin not found with id " + pinId));
    Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException("Board not found with id " + boardId));

    PinBoard pinBoard = new PinBoard();
    pinBoard.setPin(pin);
    pinBoard.setBoard(board);
    repository.save(pinBoard);

    return pinBoard;
  }

  @Override
  public void removePinFromBoard(UUID pinId, UUID boardId, User user) {
    PinBoard pinBoard = repository.findByBoardIdAndPinId(boardId, pinId).orElseThrow(() -> new PinBoardNotFoundException("PinBoard not found with pinId " + pinId + "and boardId " + boardId));
    Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException("Board not found with id " + boardId));

    if (!board.getUser().getId().equals(user.getId())) {
      throw new UnauthorizedException("Current user is not the owner of this board");
    }

    repository.delete(pinBoard);
  }
}
