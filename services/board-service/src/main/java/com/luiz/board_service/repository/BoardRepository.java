package com.luiz.board_service.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.luiz.board_service.entity.Board;

public interface BoardRepository extends JpaRepository<Board, UUID> {
  Page<Board> findByUserId(UUID userId, Pageable pageable);
}
