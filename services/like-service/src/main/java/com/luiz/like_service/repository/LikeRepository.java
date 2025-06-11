package com.luiz.like_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luiz.like_service.entity.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {
  Optional<Like> findByPinIdAndUserId(UUID pinId, UUID userId);
  Page<Like> findByPinId(UUID pinId, Pageable pageable);
  Page<Like> findByUserId(UUID userId, Pageable pageable);
}
