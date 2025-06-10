package com.luiz.board_service.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.luiz.board_service.dtos.BoardDto;
import com.luiz.board_service.dtos.BoardPostRequest;
import com.luiz.board_service.dtos.BoardUpdateRequest;

public interface BoardService {
  BoardDto createBoard(BoardPostRequest request, UUID userId);
  BoardDto updateBoard(UUID boardId, BoardUpdateRequest request, UUID userId);
  void deleteBoard(UUID boardId, UUID userId);
  BoardDto getBoard(UUID boardId);
  Page<BoardDto> getBoardsByUserId(UUID userId, Pageable pageable);
  Page<BoardDto> getBoardsByUsername(String username, Pageable pageable);
}
