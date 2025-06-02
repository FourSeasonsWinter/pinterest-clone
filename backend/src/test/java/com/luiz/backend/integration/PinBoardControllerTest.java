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
  private PinBoardRepository pinBoardRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PinRepository pinRepository;

  @Autowired
  private BoardRepository boardRepository;

  private User testUser;
  private Pin pin;
  private Board board;

  @BeforeEach
  void setUp() {
    testUser = new User();
    testUser.setUsername("Test User");
    userRepository.save(testUser);

    pin = new Pin();
    pin.setTitle("Test Pin");
    pin.setUser(testUser);
    pinRepository.save(pin);

    board = new Board();
    board.setName("Test Board");
    board.setUser(testUser);
    boardRepository.save(board);
  }

  @Test
  void shouldGetPinsFromBoard() throws Exception {
    for (int i = 1; i <= 15; i++) {
      Pin newPin = new Pin();
      newPin.setTitle("Test Pin " + i);
      newPin.setUser(testUser);
      pinRepository.save(newPin);

      PinBoard pinBoard = new PinBoard();
      pinBoard.setPin(newPin);
      pinBoard.setBoard(board);
      pinBoardRepository.save(pinBoard);
    }

    mockMvc.perform(get("/pin-board/boards/" + board.getId())
      .param("page", "0")
      .param("size", "10"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content").isArray())
      .andExpect(jsonPath("$.content.length()").value(10))
      .andExpect(jsonPath("$.content[0].title").value("Test Pin 1"))
      .andExpect(jsonPath("$.number").value(0))
      .andExpect(jsonPath("$.size").value(10))
      .andExpect(jsonPath("$.totalPages").value(2))
      .andExpect(jsonPath("$.totalElements").value(15));
  }

  @Test
  void shouldGetBoardsThatHasPin() throws Exception {
    for (int i = 1; i <= 15; i++) {
      Board newBoard = new Board();
      newBoard.setName("Test Board " + i);
      newBoard.setUser(testUser);
      boardRepository.save(newBoard);

      PinBoard pinBoard = new PinBoard();
      pinBoard.setBoard(newBoard);
      pinBoard.setPin(pin);
      pinBoardRepository.save(pinBoard);
    }

    mockMvc.perform(get("/pin-board/pins/" + pin.getId())
      .param("page", "0")
      .param("size", "10"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content").isArray())
      .andExpect(jsonPath("$.content.length()").value(10))
      .andExpect(jsonPath("$.content[0].name").value("Test Board 1"))
      .andExpect(jsonPath("$.number").value(0))
      .andExpect(jsonPath("$.size").value(10))
      .andExpect(jsonPath("$.totalPages").value(2))
      .andExpect(jsonPath("$.totalElements").value(15));
  }

  @Test
  @WithMockUser(username = "Test User")
  void shouldAddPinToBoard() throws Exception {
    PinBoardPostRequest request = new PinBoardPostRequest();
    request.setPinId(pin.getId());
    request.setBoardId(board.getId());

    assertTrue(pinBoardRepository.findAll().isEmpty());

    mockMvc.perform(post("/pin-board")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(request))
      .with(user("Test User")))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.pinId").value(pin.getId().toString()))
      .andExpect(jsonPath("$.boardId").value(board.getId().toString()));
    
    assertTrue(pinBoardRepository.findAll().size() == 1);
    assertTrue(pinBoardRepository.findAll().get(0).getPin().getTitle() == "Test Pin");
  }

  @Test
  @WithMockUser(username = "Test User")
  void shouldRemovePinFromBoard() throws Exception {
    PinBoard testPinBoard = new PinBoard();
    testPinBoard.setPin(pin);
    testPinBoard.setBoard(board);
    pinBoardRepository.save(testPinBoard);

    mockMvc.perform(delete("/pin-board")
      .param("boardId", board.getId().toString())
      .param("pinId", pin.getId().toString())
      .with(user("Test User")))
      .andExpect(status().isNoContent());
    
    assertTrue(pinBoardRepository.findById(testPinBoard.getId()).isEmpty());
  }
}
