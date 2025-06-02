package com.luiz.backend.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.luiz.backend.entity.Like;
import com.luiz.backend.entity.Pin;
import com.luiz.backend.entity.User;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {
  Like findByPinIdAndUserId(UUID pinId, UUID userId);

  @Query("SELECT lk.user FROM Like lk JOIN lk.pin p WHERE p.id = :pinId")
  Page<User> findUsersByPinId(@Param("pinId") UUID pinId, Pageable pageable);

  @Query("SELECT lk.pin FROM Like lk JOIN lk.user u WHERE u.id = :userId")
  Page<Pin> findPinsByUserId(@Param("userId") UUID userId, Pageable pageable);
}
