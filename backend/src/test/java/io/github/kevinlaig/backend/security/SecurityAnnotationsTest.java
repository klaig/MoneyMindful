package io.github.kevinlaig.backend.security;

import io.github.kevinlaig.backend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import io.github.kevinlaig.backend.service.UserService;

import java.security.Principal;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SecurityAnnotationsTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  // Admin access tests
  @Test
  @WithMockUser(roles = "ADMIN")
  void whenAdminAccessingAdminEndpoint_thenAccessIsGranted() throws Exception {
    mockMvc.perform(get("/api/admin/users"))
      .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "USER")
  void whenUserAccessingAdminEndpoint_thenAccessIsDenied() throws Exception {
    mockMvc.perform(get("/api/admin/users"))
      .andExpect(status().isForbidden());
  }

  // User access test
  @Test
  @WithMockUser(username = "user", roles = "USER")
  void whenUserAccessingUserEndpoint_thenAccessIsGranted() throws Exception {
    Principal mockPrincipal = mock(Principal.class);
    when(mockPrincipal.getName()).thenReturn("user");

    // Mock the user service to return a valid user
    when(userService.findUserByUsername("user")).thenReturn(Optional.of(new User()));

    mockMvc.perform(get("/api/user/profile").principal(mockPrincipal))
      .andExpect(status().isOk());
  }

  // Forbidden access tests
  @Test
  void whenAnonymousAccessingProtectedEndpoint_thenAccessIsDenied() throws Exception {
    mockMvc.perform(get("/api/user/expenses"))
      .andExpect(status().isForbidden());
    mockMvc.perform(get("/api/admin/users"))
      .andExpect(status().isForbidden());
  }
}
