package com.luiz.backend.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.luiz.backend.entity.Like;
import com.luiz.backend.entity.Pin;
import com.luiz.backend.entity.User;
import com.luiz.backend.repository.LikeRepository;
import com.luiz.backend.repository.PinRepository;
import com.luiz.backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class LikeControllerTest {
  
  @Autowired private MockMvc mockMvc;
  @Autowired private LikeRepository likeRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private PinRepository pinRepository;

  private User user1;
  private User user2;
  private Pin testPin;
  private Like testLike;

  @BeforeEach
  void setUp() {
    user1 = new User();
    user1.setUsername("User 1");
    userRepository.save(user1);

    testPin = new Pin();
    testPin.setUser(user1);
    testPin.setTitle("Test Pin");
    pinRepository.save(testPin);

    user2 = new User();
    user2.setUsername("User 2");
    userRepository.save(user2);

    testLike.setPin(testPin);
    testLike.setUser(user2);
    likeRepository.save(testLike);
  }

  @Test
  void shouldGetUsersWhoLikedPin() throws Exception {
    for (int i = 1; i <= 15; i++) {
      User user = new User();
      user.setUsername("Test User " + i);
      userRepository.save(user);

      Like like = new Like();
      like.setPin(testPin);
      like.setUser(user);
      likeRepository.save(like);
    }

    mockMvc.perform(get("/likes/pin/" + testPin.getId())
      .param("page", "0")
      .param("size", "10"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content").isArray())
      .andExpect(jsonPath("$.content.length()").value(10))
      .andExpect(jsonPath("$.content[0].name").value("Test User 1"))
      .andExpect(jsonPath("$.number").value(0))
      .andExpect(jsonPath("$.size").value(10))
      .andExpect(jsonPath("$.totalPages").value(2))
      .andExpect(jsonPath("$.totalElements").value(15));
  }

  @Test
  void shouldGetPinsLikedByUser() throws Exception {
    for (int i = 1; i <= 15; i++) {
      Pin pin = new Pin();
      pin.setUser(user1);
      pin.setTitle("Test Pin " + i);

      Like like = new Like();
      like.setPin(pin);
      like.setUser(user1);
      likeRepository.save(like);
    }

    mockMvc.perform(get("/likes/user/" + user1.getId())
      .param("page", "0")
      .param("size", "10"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content").isArray())
      .andExpect(jsonPath("$.content.length()").value(10))
      .andExpect(jsonPath("$.content[0].name").value("Test Pin 1"))
      .andExpect(jsonPath("$.number").value(0))
      .andExpect(jsonPath("$.size").value(10))
      .andExpect(jsonPath("$.totalPages").value(2))
      .andExpect(jsonPath("$.totalElements").value(15));
  }

  @Test
  @WithMockUser("User 2")
  void shouldLikePin() throws Exception {
    mockMvc.perform(post("/likes/" + testPin.getId())
      .with(user("User 2")))
      .andExpect(status().isCreated());

    assertTrue(likeRepository.findAll().size() > 1);
    assertTrue(likeRepository.findAll().get(1).getPin().equals(testPin));
    assertTrue(likeRepository.findAll().get(1).getUser().equals(user2));
    assertTrue(testPin.getLikesCount() == 2);
  }
  
  @Test
  @WithMockUser("User 1")
  void shouldUnlikePin() throws Exception {
    mockMvc.perform(delete("/likes/" + testPin.getId())
      .with(user("User 1")))
      .andExpect(status().isNoContent());

    assertTrue(likeRepository.count() == 0);
    assertTrue(testPin.getLikesCount() == 0);
  }
}
