package io.github.kevinlaig.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kevinlaig.backend.dto.LoginRequest;
import io.github.kevinlaig.backend.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test for the AuthController.
 */
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private JwtUtil jwtUtil;

  @InjectMocks
  private AuthController authController;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  public void whenLoginWithValidCredentials_thenReturnJwtToken() throws Exception {
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setUsername("testUsername");
    loginRequest.setPassword("testPassword");
    String mockJwtToken = "mockJwtToken";
    Authentication authentication = mock(Authentication.class);

    when(authenticationManager.authenticate(any())).thenReturn(authentication);
    when(authentication.getName()).thenReturn("testUsername");
    when(jwtUtil.generateToken("testUsername")).thenReturn(mockJwtToken);

    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value(mockJwtToken));
  }

  @Test
  public void whenLogout_thenSuccessResponse() throws Exception {
    mockMvc.perform(post("/api/auth/logout"))
      .andExpect(status().isOk())
      .andExpect(content().string("Logout successful!"));
  }

  // Helper method to convert objects to JSON string
  private String toJson(Object obj) throws Exception {
    return objectMapper.writeValueAsString(obj);
  }
}

