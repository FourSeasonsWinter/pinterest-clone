package com.luiz.backend.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.luiz.backend.entity.Board;
import com.luiz.backend.entity.User;

public interface BoardRepository extends JpaRepository<Board, UUID> {
  Page<Board> findByUser(User user, Pageable pageable);
}
