package com.luiz.backend.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.luiz.backend.entity.Follow;
import com.luiz.backend.entity.User;

public interface FollowRepository extends JpaRepository<Follow, UUID> {
  @Query("SELECT f.follower FROM Follow f WHERE f.followedBy.id = :userId")
  Page<User> findFollowersByUserId(@Param("userId") UUID userId, Pageable pageable);

  @Query("SELECT f.followedBy FROM Follow f WHERE f.follower.id = :userId")
  Page<User> findFollowingByUserId(@Param("userId") UUID userId, Pageable pageable);

  Follow findByFollowerIdAndFollowedById(UUID followerId, UUID followedById);
}
