package com.luiz.follow_service.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.luiz.follow_service.entity.Follow;


public interface FollowRepository extends JpaRepository<Follow, UUID> {
  Follow findByFollowerIdAndFollowedById(UUID followerId, UUID followedById);
  Page<Follow> findByFollowedById(UUID followedById, Pageable pageable);
  Page<Follow> findByFollowerId(UUID followerId, Pageable pageable);
  long countByFollowerId(UUID id);
  long countByFollowedById(UUID id);
}
