package com.luiz.backend.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.luiz.backend.entity.Board;
import com.luiz.backend.entity.Pin;
import com.luiz.backend.entity.User;

public interface BoardRepository extends JpaRepository<Board, UUID> {
  Page<Board> findByUser(User user, Pageable pageable);

  Page<Board> findByUser_Id(UUID userId, Pageable pageable);

  Page<Board> findByUser_Username(String username, Pageable pageable);

  @Query("SELECT p FROM Pin p JOIN p.boards b WHERE b.id = :boardId")
  Page<Pin> findPinsByBoardId(@Param("boardId") UUID boardId, Pageable pageable);
}
