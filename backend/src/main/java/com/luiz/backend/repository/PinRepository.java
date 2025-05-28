package com.luiz.backend.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.luiz.backend.entity.Pin;
import com.luiz.backend.entity.User;

public interface PinRepository extends JpaRepository<Pin, UUID> {
  Page<Pin> findByTags(String tags, Pageable pageable);
  Page<Pin> findByUser(User user, Pageable pageable);
  Page<Pin> findByUser_Id(UUID userId, Pageable pageable);
}
