package io.github.kevinlaig.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kevinlaig.backend.dto.CreateSavingsGoalDto;
import io.github.kevinlaig.backend.dto.UpdateSavingsGoalDto;
import io.github.kevinlaig.backend.model.SavingsGoal;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.service.SavingsGoalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class SavingsGoalControllerTest {

  @Mock
  private SavingsGoalService savingsGoalService;

  @InjectMocks
  private SavingsGoalController savingsGoalController;

  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private User user;
  private SavingsGoal savingsGoal;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(savingsGoalController).build();
    user = new User(1L, "user@example.com", "testUser", "password", "Test User", null);
    savingsGoal = new SavingsGoal(1L, user, "Vacation", BigDecimal.valueOf(2000), BigDecimal.ZERO, LocalDate.now().plusMonths(6));
  }

  @Test
  void createSavingsGoal_ValidData_ReturnsCreatedGoal() throws Exception {
    CreateSavingsGoalDto createDto = new CreateSavingsGoalDto();
    createDto.setName("Vacation");
    createDto.setTargetAmount(BigDecimal.valueOf(2000));
    createDto.setTargetDate(LocalDate.now().plusMonths(6));
    when(savingsGoalService.createSavingsGoal(any(CreateSavingsGoalDto.class), any(User.class))).thenReturn(savingsGoal);

    mockMvc.perform(post("/api/user/savingsgoals")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createDto))
        .principal(() -> "testUser"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("Vacation"));
  }

  @Test
  void getAllUserSavingsGoals_ReturnsGoalsList() throws Exception {
    when(savingsGoalService.getAllUserSavingsGoals(user)).thenReturn(Collections.singletonList(savingsGoal));

    mockMvc.perform(get("/api/user/savingsgoals")
        .principal(() -> "testUser"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].name").value("Vacation"));
  }

  @Test
  void getSavingsGoalById_ExistingId_ReturnsGoal() throws Exception {
    when(savingsGoalService.findSavingsGoalById(1L, user)).thenReturn(Optional.of(savingsGoal));

    mockMvc.perform(get("/api/user/savingsgoals/{id}", 1)
        .principal(() -> "testUser"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("Vacation"));
  }

  @Test
  void updateSavingsGoal_ExistingGoal_UpdatesAndReturnsUpdatedGoal() throws Exception {
    UpdateSavingsGoalDto updateDto = new UpdateSavingsGoalDto();
    updateDto.setName("Updated Vacation");
    updateDto.setTargetAmount(BigDecimal.valueOf(2500));
    updateDto.setCurrentAmount(BigDecimal.valueOf(500));
    updateDto.setTargetDate(LocalDate.now().plusMonths(12));
    when(savingsGoalService.updateSavingsGoal(1L, updateDto, user)).thenReturn(Optional.of(new SavingsGoal(1L, user, "Updated Vacation", BigDecimal.valueOf(2500), BigDecimal.valueOf(500), LocalDate.now().plusMonths(12))));

    mockMvc.perform(put("/api/user/savingsgoals/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateDto))
        .principal(() -> "testUser"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("Updated Vacation"));
  }

  @Test
  void deleteSavingsGoal_ExistingId_DeletesGoal() throws Exception {
    when(savingsGoalService.deleteSavingsGoal(1L, user)).thenReturn(true);

    mockMvc.perform(delete("/api/user/savingsgoals/{id}", 1)
        .principal(() -> "testUser"))
      .andExpect(status().isOk());
  }
}
