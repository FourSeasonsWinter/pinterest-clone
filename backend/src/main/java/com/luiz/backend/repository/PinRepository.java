package com.luiz.backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.luiz.backend.entity.Pin;
import com.luiz.backend.entity.User;

public interface PinRepository extends JpaRepository<Pin, UUID> {
  Page<Pin> findByTag(String tag, Pageable pageable);
  Page<Pin> findByUser(User user, Pageable pageable);
  Page<Pin> findByUser_Id(UUID userId, Pageable pageable);

  @Query("SELECT p FROM Pin p WHERE p.user IN :users")
  Page<Pin> findByUsers(@Param("users") List<User> followedUsers, Pageable pageable);
}
