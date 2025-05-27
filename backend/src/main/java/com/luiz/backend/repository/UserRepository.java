package com.luiz.backend.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.luiz.backend.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
  @EntityGraph(attributePaths = {"boards", "pins", "likes", "comments"})
  Optional<User> findByUsername(String username);
}
