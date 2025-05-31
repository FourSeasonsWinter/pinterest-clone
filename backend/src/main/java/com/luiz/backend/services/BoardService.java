package com.luiz.backend.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.luiz.backend.dtos.BoardDto;
import com.luiz.backend.dtos.BoardPostRequest;
import com.luiz.backend.dtos.BoardUpdateRequest;
import com.luiz.backend.entity.User;

public interface BoardService {
  BoardDto createBoard(BoardPostRequest request, User user);
  BoardDto updateBoard(UUID boardId, BoardUpdateRequest request, User user);
  void deleteBoard(UUID boardId, User user);
  BoardDto getBoard(UUID boardId);
  Page<BoardDto> getBoardsByUser(User user, Pageable pageable);
}
