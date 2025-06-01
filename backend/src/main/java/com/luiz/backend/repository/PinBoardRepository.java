package com.luiz.backend.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.luiz.backend.entity.Board;
import com.luiz.backend.entity.Pin;
import com.luiz.backend.entity.PinBoard;

public interface PinBoardRepository extends JpaRepository<PinBoard, UUID> {
  Optional<PinBoard> findByBoardIdAndPinId(UUID boardId, UUID pinId);

  Page<PinBoard> findByBoardId(UUID boardId, Pageable pageable);

  Page<PinBoard> findByPinId(UUID pinId, Pageable pageable);

  @Query("SELECT p FROM PinBoard pb JOIN pb.pin p WHERE pb.board.id = :boardId")
  Page<Pin> findPinsByBoardId(@Param("boardId") UUID boardId, Pageable pageable);

  @Query("SELECT b FROM PinBoard pb JOIN pb.board b WHERE pb.pin.id = :pinId")
  Page<Board> findBoardsByPinId(@Param("pinId") UUID pinId, Pageable pageable);
}
