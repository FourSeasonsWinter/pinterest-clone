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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luiz.backend.dtos.PinBoardPostRequest;
import com.luiz.backend.entity.Board;
import com.luiz.backend.entity.Pin;
import com.luiz.backend.entity.PinBoard;
import com.luiz.backend.entity.User;
import com.luiz.backend.repository.BoardRepository;
import com.luiz.backend.repository.PinBoardRepository;
import com.luiz.backend.repository.PinRepository;
import com.luiz.backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class PinBoardControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private PinBoardRepository repository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PinRepository pinRepository;

  @Autowired
  private BoardRepository boardRepository;

  private User user;
  private Pin pin;
  private Board board;
  private PinBoard testPinBoard;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setUsername("Test User");
    userRepository.save(user);

    pin = new Pin();
    pin.setTitle("Test Pin");
    pin.setUser(user);
    pinRepository.save(pin);

    board = new Board();
    board.setName("Test Board");
    board.setUser(user);
    boardRepository.save(board);

    testPinBoard = new PinBoard();
    testPinBoard.setPin(pin);
    testPinBoard.setBoard(board);
    repository.save(testPinBoard);
  }

  @Test
  void shouldGetPinsFromBoard() throws Exception {
    for (int i = 1; i < 15; i++) {
      Pin newPin = new Pin();
      newPin.setUser(user);

      PinBoard pinBoard = new PinBoard();
      pinBoard.setPin(newPin);
      pinBoard.setBoard(board);
      repository.save(pinBoard);
    }

    mockMvc.perform(get("/pin-board/boards/" + board.getId())
      .param("page", "0")
      .param("size", "10"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content").isArray())
      .andExpect(jsonPath("$.content.length()").value(10))
      .andExpect(jsonPath("$.content[0].pin.title").value("Test Pin"))
      .andExpect(jsonPath("$.number").value(0))
      .andExpect(jsonPath("$.size").value(10))
      .andExpect(jsonPath("$.totalPages").value(2))
      .andExpect(jsonPath("$.totalElements").value(16));
  }

  @Test
  void shouldGetBoardsThatHasPin() throws Exception {
    for (int i = 1; i <= 15; i++) {
      Board newBoard = new Board();
      PinBoard pinBoard = new PinBoard();

      pinBoard.setBoard(newBoard);
      pinBoard.setPin(pin);
      repository.save(pinBoard);
    }

    mockMvc.perform(get("/pin-board/pins/" + pin.getId())
      .param("page", "0")
      .param("size", "10"))
      .andExpect(jsonPath("$.content").isArray())
      .andExpect(jsonPath("$.content.length()").value(10))
      .andExpect(jsonPath("$.content[0].board.name").value("Test Board"))
      .andExpect(jsonPath("$.number").value(0))
      .andExpect(jsonPath("$.size").value(10))
      .andExpect(jsonPath("$.totalPages").value(2))
      .andExpect(jsonPath("$.totalElements").value(16));
  }

  @Test
  @WithMockUser(username = "Test User")
  void shouldAddPinToBoard() throws Exception {
    PinBoardPostRequest request = new PinBoardPostRequest();
    request.setPinId(pin.getId());
    request.setBoardId(board.getId());

    mockMvc.perform(post("/pin-board")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(request))
      .with(user("Test User")))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.pinId").value(pin.getId()))
      .andExpect(jsonPath("$.boardId").value(board.getId()));
    
    assertTrue(repository.findByBoardIdAndPinId(pin.getId(), board.getId()).isPresent());
  }

  @Test
  @WithMockUser(username = "Test User")
  void shouldRemovePinFromBoard() throws Exception {
    mockMvc.perform(delete("/pin-board/" + testPinBoard.getId())
      .with(user("Test User")))
      .andExpect(status().isNoContent());
    
    assertTrue(repository.findById(testPinBoard.getId()).isEmpty());
  }
}
