package com.luiz.backend.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.luiz.backend.dtos.PinDto;
import com.luiz.backend.entity.Pin;
import com.luiz.backend.entity.User;
import com.luiz.backend.repository.PinRepository;
import com.luiz.backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PinControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private PinRepository pinRepository;

  @Autowired
  private UserRepository userRepository;

  private Pin testPin;
  private User testUser;

  @BeforeEach
  void setUp() {
    testUser = new User();
    testUser.setUsername("Test User");
    testUser.setEmail("test@example.com");
    userRepository.save(testUser);

    testPin = new Pin();
    testPin.setTitle("Test Pin");
    testPin.setUser(testUser);
    pinRepository.save(testPin);
  }

  @Test
  void shouldGetPinById() throws Exception {
    mockMvc.perform(get("/pins/" + testPin.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Test Pin"));
  }

  @Test
  void shouldGetPinsByUser() throws Exception {
    for (int i = 1; i <= 15; i++) {
      Pin pin = new Pin();
      pin.setTitle("Test Pin " + i);
      pin.setUser(testUser);
      pinRepository.save(pin);
    }

    mockMvc.perform(get("/pins/by-user")
        .param("username", "Test User")
        .param("page", "0")
        .param("size", "10")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(10))
        .andExpect(jsonPath("$.content[0].title").value("Test Pin")) // Verify the first item's title
        .andExpect(jsonPath("$.number").value(0)) // Verify current page number
        .andExpect(jsonPath("$.size").value(10))
        .andExpect(jsonPath("$.totalElements").value(16))
        .andExpect(jsonPath("$.totalPages").value(2));
  }

  @Test
  @WithMockUser(username = "Test User")
  void shouldCreatePin() throws Exception {
    PinDto pin = new PinDto();
    pin.setTitle("New Pin");

    mockMvc.perform(post("/pins")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(pin))
        .with(user(testUser.getUsername())))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title").value("New Pin"));
  }

  @Test
  @WithMockUser(username = "Test User")
  void shouldUpdatePin() throws Exception {
    PinDto updatedPin = new PinDto();
    updatedPin.setTitle("Updated Pin");

    mockMvc.perform(put("/pins/" + testPin.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updatedPin))
        .with(user(testUser.getUsername())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Updated Pin"));
  }

  @Test
  @WithMockUser(username = "Test User")
  void shouldDeletePin() throws Exception {
    mockMvc.perform(delete("/pins/" + testPin.getId())
        .with(user(testUser.getUsername())))
        .andExpect(status().isNoContent());

    assertTrue(pinRepository.findById(testPin.getId()).isEmpty());
  }
}
