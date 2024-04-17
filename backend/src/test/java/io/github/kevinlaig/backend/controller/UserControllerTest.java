package io.github.kevinlaig.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kevinlaig.backend.dto.SignupDto;
import io.github.kevinlaig.backend.dto.UpdateUserDto;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test for the UserController.
 */
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  void createUser_WithValidSignupDto_CreatesAndReturnsNewUser() throws Exception {
    SignupDto signupDto = new SignupDto();
    signupDto.setUsername("newUser");
    signupDto.setPassword("password");
    signupDto.setEmail("newuser@example.com");
    signupDto.setFullName("New User");

    User createdUser = new User(null, "newuser@example.com", "newUser", "password", "New User", null);

    when(userService.createUser(any(SignupDto.class))).thenReturn(createdUser);

    mockMvc.perform(post("/api/user/signup")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(signupDto)))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.username").value("newUser"));
  }

  @Test
  void getUserById_WithValidId_ReturnsUser() throws Exception {
    User foundUser = new User(1L, "existing@example.com", "existingUser", "password", "Existing User", null);
    when(userService.findUserById(1L)).thenReturn(Optional.of(foundUser));

    mockMvc.perform(get("/api/admin/user/{id}", 1))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.username").value("existingUser"));
  }

  @Test
  void getAllUsers_WhenRequested_ReturnsAllUsers() throws Exception {
    User user1 = new User(1L, "user1@example.com", "user1", "password", "User One", null);
    User user2 = new User(2L, "user2@example.com", "user2", "password", "User Two", null);
    when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

    mockMvc.perform(get("/api/admin/users"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].username").value("user1"))
      .andExpect(jsonPath("$[1].username").value("user2"));
  }

  @Test
  void updateUser_WithValidData_UpdatesAndReturnsUser() throws Exception {
    UpdateUserDto updateUserDto = new UpdateUserDto();
    updateUserDto.setUsername("existingUser");
    updateUserDto.setPassword("newpassword");
    updateUserDto.setEmail("existing@example.com");
    updateUserDto.setFullName("Existing User Updated");

    User updatedUser = new User(1L, "existing@example.com", "existingUser", "newpassword", "Existing User Updated", null);
    Principal mockPrincipal = mock(Principal.class);
    when(mockPrincipal.getName()).thenReturn("existingUser");

    when(userService.updateUser(any(UpdateUserDto.class))).thenReturn(updatedUser);

    mockMvc.perform(put("/api/user/profile")
        .principal(mockPrincipal)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateUserDto)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.fullName").value("Existing User Updated"));
  }

  @Test
  void deleteUser_WithValidId_DeletesUser() throws Exception {
    when(userService.deleteUser(1L)).thenReturn(true);

    mockMvc.perform(delete("/api/admin/user/{id}", 1))
      .andExpect(status().isOk());
  }
}
