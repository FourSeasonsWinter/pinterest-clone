package com.luiz.backend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luiz.backend.entity.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {
  
}
