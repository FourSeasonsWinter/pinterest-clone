package com.luiz.backend.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.luiz.backend.controllers.PinController;
import com.luiz.backend.dtos.PinDto;
import com.luiz.backend.exception.PinNotFoundException;
import com.luiz.backend.services.PinService;

@ActiveProfiles("test")
@WebMvcTest(PinController.class)
public class PinControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private PinService service;

  @Test
  void getPin_Success() throws Exception {
    UUID pinId = UUID.randomUUID();
    PinDto pinDto = new PinDto();
    pinDto.setId(pinId);

    when(service.getPin(pinId)).thenReturn(pinDto);

    mockMvc.perform(get("/pins/{id}", pinId)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(pinId.toString()));

    verify(service, times(1)).getPin(pinId);
  }

  @Test
  void testGetPin_NotFound() throws Exception {
    UUID pinId = UUID.randomUUID();

    when(service.getPin(pinId)).thenThrow(new PinNotFoundException("Pin not found"));

    mockMvc.perform(get("/pins/{id}", pinId)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());

    verify(service, times(1)).getPin(pinId);
  }
}
