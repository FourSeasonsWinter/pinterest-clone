package com.luiz.backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.luiz.backend.entity.Follow;
import com.luiz.backend.entity.User;

public interface FollowRepository extends JpaRepository<Follow, UUID>  {
  @Query("SELECT f.followed FROM Follow f WHERE f.follower.id = :userId")
  List<User> findFollowedByUserId(@Param("userId") UUID userId);
}
