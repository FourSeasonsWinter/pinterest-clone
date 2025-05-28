package com.luiz.backend.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.luiz.backend.controllers.UserController;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
class SecurityTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserController userController;

  @Test
  @WithMockUser(username = "testUser")
  void testGetCurrentUser_Authenticated() throws Exception {
    mockMvc.perform(get("/users/me"))
        .andExpect(status().isOk());
  }

  @Test
  void testGetCurrentUser_Unauthenticated() throws Exception {
    mockMvc.perform(get("/users/me"))
        .andExpect(status().isUnauthorized());
  }
}