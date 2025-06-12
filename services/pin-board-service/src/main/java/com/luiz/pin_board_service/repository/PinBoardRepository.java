package com.luiz.pin_board_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.luiz.pin_board_service.entity.PinBoard;


public interface PinBoardRepository extends JpaRepository<PinBoard, UUID> {
  Optional<PinBoard> findByBoardIdAndPinId(UUID boardId, UUID pinId);
  
  Page<PinBoard> findByBoardId(UUID boardId, Pageable pageable);
  Page<PinBoard> findByPinId(UUID pinId, Pageable pageable);
}
