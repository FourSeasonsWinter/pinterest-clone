package com.luiz.pin_board_service.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.luiz.pin_board_service.dtos.BoardDto;
import com.luiz.pin_board_service.dtos.PinDto;
import com.luiz.pin_board_service.entity.PinBoard;

public interface PinBoardService {
  Page<PinDto> getPinsOnBoard(UUID boardId, Pageable page);
  Page<BoardDto> getBoardsWithPin(UUID pinId, Pageable page);
  PinBoard addPinToBoard(UUID pinId, UUID boardId, UUID userId);
  void removePinFromBoard(UUID pinId, UUID boardId, UUID userId);
}
