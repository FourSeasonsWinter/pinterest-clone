package com.luiz.backend.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.luiz.backend.entity.Pin;
import com.luiz.backend.entity.User;

public interface PinRepository extends JpaRepository<Pin, UUID> {
  Page<Pin> findByTag(String tag, Pageable pageable);
  Page<Pin> findByUser(User user, Pageable pageable);
}
