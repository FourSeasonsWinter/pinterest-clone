package com.luiz.backend.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.luiz.backend.dtos.BoardDto;
import com.luiz.backend.dtos.PageDto;
import com.luiz.backend.dtos.PinDto;
import com.luiz.backend.entity.User;

@Service
public class PinBoardServiceImpl implements PinBoardService {

  @Override
  public PageDto<PinDto> getPinsFromBoard(UUID boardId, Pageable page) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getPinsFromBoard'");
  }

  @Override
  public PageDto<BoardDto> getBoardsFromPin(UUID pinId, Pageable page) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getBoardsFromPin'");
  }

  @Override
  public void addPinToBoard(UUID pinId, UUID boardId, User user) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'addPinToBoard'");
  }

  @Override
  public void removePinFromBoard(UUID pinBoardId, User user) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'removePinFromBoard'");
  }
}
