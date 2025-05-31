package com.luiz.backend.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luiz.backend.entity.Board;
import com.luiz.backend.entity.User;
import com.luiz.backend.repository.BoardRepository;
import com.luiz.backend.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BoardControllerTest {
  
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private BoardRepository boardRepository;

  @Autowired
  private UserRepository userRepository;

  private Board testBoard;
  private User testUser;

  @BeforeEach
  void setUp() {
    testUser = new User();
    testUser.setUsername("Test User");
    userRepository.save(testUser);

    testBoard = new Board();
    testBoard.setName("Test Board");
    testBoard.setUser(testUser);
    boardRepository.save(testBoard);
  }

  @Test
  void shouldGetBoardById() throws Exception {
    mockMvc.perform(get("/boards/" + testBoard.getId())
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("Test Board"))
      .andExpect(jsonPath("$.userId").value(testUser.getId().toString()));
  }

  @Test
  void shouldGetBoardsByUser() throws Exception {
    for (int i = 1; i <= 15; i++) {
      Board board = new Board();
      board.setName("Test Board " + i);
      board.setUser(testUser);
      boardRepository.save(board);
    }

    mockMvc.perform(get("/boards/by-user")
      .param("username", "Test User")
      .param("page", "0")
      .param("size", "10")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content").isArray())
      .andExpect(jsonPath("$.content.length()").value(10))
      .andExpect(jsonPath("$.content[0].name").value("Test Board"))
      .andExpect(jsonPath("$.number").value(0))
      .andExpect(jsonPath("$.size").value(10))
      .andExpect(jsonPath("$.totalPages").value(2))
      .andExpect(jsonPath("$.totalElements").value(16));
  }
}
