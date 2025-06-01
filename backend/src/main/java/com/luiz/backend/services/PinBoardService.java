package com.luiz.backend.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.luiz.backend.dtos.BoardDto;
import com.luiz.backend.dtos.PinDto;
import com.luiz.backend.entity.PinBoard;
import com.luiz.backend.entity.User;

public interface PinBoardService {
  Page<PinDto> getPinsFromBoard(UUID boardId, Pageable page);
  Page<BoardDto> getBoardsFromPin(UUID pinId, Pageable page);
  PinBoard addPinToBoard(UUID pinId, UUID boardId, User user);
  void removePinFromBoard(UUID pinId, UUID boardId, User user);
}
