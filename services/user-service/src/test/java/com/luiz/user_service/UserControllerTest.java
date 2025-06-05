package com.luiz.user_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.luiz.user_service.entity.User;
import com.luiz.user_service.repository.UserRepository;

import jakarta.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
  
  @Autowired MockMvc mockMvc;
  @Autowired UserRepository userRepository;

  User testUser;

  @BeforeEach
  public void setUp() {
    testUser = new User();
    testUser.setUsername("TestUser");
    userRepository.save(testUser);
  }

  @Test
  public void testGetUserById() throws Exception {
    mockMvc.perform(get("/users/TestUser"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(testUser.getId().toString()))
      .andExpect(jsonPath("$.username").value("TestUser"));
  }
}
