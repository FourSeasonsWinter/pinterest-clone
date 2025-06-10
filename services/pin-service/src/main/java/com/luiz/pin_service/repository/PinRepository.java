package com.luiz.pin_service.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.luiz.pin_service.entity.Pin;

public interface PinRepository extends JpaRepository<Pin, UUID> {
  Page<Pin> findByUserId(UUID userId, Pageable pageable);
}
