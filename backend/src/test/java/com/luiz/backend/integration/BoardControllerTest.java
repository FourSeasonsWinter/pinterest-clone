package com.luiz.backend.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luiz.backend.dtos.BoardDto;
import com.luiz.backend.dtos.BoardUpdateRequest;
import com.luiz.backend.entity.Board;
import com.luiz.backend.entity.User;
import com.luiz.backend.repository.BoardRepository;
import com.luiz.backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
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

  @Test
  @WithMockUser(username = "Test User")
  void shouldCreateBoard() throws Exception {
    BoardDto board = new BoardDto();
    board.setName("New Board");

    mockMvc.perform(post("/boards")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(board))
      .with(user("Test User")))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.name").value("New Board"))
      .andExpect(jsonPath("$.userId").value(testUser.getId().toString()));
  }

  @Test
  @WithMockUser(username = "Test User")
  void shouldUpdateBoard() throws Exception {
    BoardUpdateRequest request = new BoardUpdateRequest();
    request.setName("Updated Board");
    request.setDescription("Updated Description");
    request.setPrivate(true);

    mockMvc.perform(put("/boards/" + testBoard.getId())
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(request))
      .with(user("Test User")))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("Updated Board"))
      .andExpect(jsonPath("$.description").value("Updated Description"))
      .andExpect(jsonPath("$.private").value(true));
  }

  @Test
  @WithMockUser(username = "Test User")
  void shouldDeleteBoard() throws Exception {
    mockMvc.perform(delete("/boards/" + testBoard.getId())
      .with(user("Test User")))
      .andExpect(status().isNoContent());
    
    assertTrue(boardRepository.findById(testBoard.getId()).isEmpty());
  }
}
