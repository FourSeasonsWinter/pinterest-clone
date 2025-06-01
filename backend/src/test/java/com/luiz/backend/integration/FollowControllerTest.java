package com.luiz.backend.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.luiz.backend.entity.Follow;
import com.luiz.backend.entity.User;
import com.luiz.backend.repository.FollowRepository;
import com.luiz.backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FollowControllerTest {
  
  @Autowired private MockMvc mockMvc;
  @Autowired private FollowRepository followRepository;
  @Autowired private UserRepository userRepository;

  private User user1;
  private User user2;
  private User user3;
  private Follow testFollow;

  @BeforeEach
  void setUp() {
    user1 = new User();
    user1.setUsername("User 1");
    user2 = new User();
    user2.setUsername("User 2");
    user3 = new User();
    user3.setUsername("User 3");
    userRepository.saveAll(List.of(user1, user2, user3));

    testFollow = new Follow();
    testFollow.setFollower(user1);
    testFollow.setFollowedBy(user3);
  }

  @Test
  void shouldGetFollowersOfUser() throws Exception {
    for (int i = 1; i < 15; i++) {
      User user = new User();
      user.setUsername("Test User " + 1);
      userRepository.save(user);

      Follow follow = new Follow();
      follow.setFollower(user);
      follow.setFollowedBy(user2);
      followRepository.save(follow);
    }

    mockMvc.perform(get("/follows/followers/" + user2.getId())
      .param("page", "0")
      .param("size", "10"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content").isArray())
      .andExpect(jsonPath("$.content[0].username").value("Test User 1"))
      .andExpect(jsonPath("$.number").value(0))
      .andExpect(jsonPath("$.size").value(15))
      .andExpect(jsonPath("$.totalPages").value(2))
      .andExpect(jsonPath("$.totalElements").value(15));
  }

  @Test
  void shouldGetUsersFollowedByUser() throws Exception {
    for (int i = 1; i < 15; i++) {
      User user = new User();
      user.setUsername("Test User " + 1);
      userRepository.save(user);

      Follow follow = new Follow();
      follow.setFollower(user2);
      follow.setFollowedBy(user);
      followRepository.save(follow);
    }

    mockMvc.perform(get("/follows/followed-by/" + user2.getId())
      .param("page", "0")
      .param("size", "10"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content").isArray())
      .andExpect(jsonPath("$.content[0].username").value("Test User 1"))
      .andExpect(jsonPath("$.number").value(0))
      .andExpect(jsonPath("$.size").value(15))
      .andExpect(jsonPath("$.totalPages").value(2))
      .andExpect(jsonPath("$.totalElements").value(15));
  }

  @Test
  @WithMockUser(username = "User 1")
  void shouldFollowUser() throws Exception {
    mockMvc.perform(post("/follows/" + user2.getId())
      .with(user("User 1")))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.followerId").value(user1.getId().toString()))
      .andExpect(jsonPath("$.followedById").value(user2.getId().toString()));

    assertFalse(followRepository.findAll().size() < 2);
  }

  @Test
  @WithMockUser(username = "User 1")
  void shouldUnfollowUser() throws Exception {
    mockMvc.perform(delete("/follows/" + user3.getId())
      .with(user("User 1")))
      .andExpect(status().isNoContent());
    
    assertTrue(followRepository.findAll().isEmpty());
  }
}
