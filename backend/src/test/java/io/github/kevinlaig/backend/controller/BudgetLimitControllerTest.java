package io.github.kevinlaig.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kevinlaig.backend.dto.CreateBudgetLimitDto;
import io.github.kevinlaig.backend.dto.UpdateBudgetLimitDto;
import io.github.kevinlaig.backend.model.Budget;
import io.github.kevinlaig.backend.model.BudgetLimit;
import io.github.kevinlaig.backend.model.Category;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.BudgetRepository;
import io.github.kevinlaig.backend.repository.UserRepository;
import io.github.kevinlaig.backend.service.BudgetLimitService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BudgetLimitControllerTest {

  @Mock
  private BudgetLimitService budgetLimitService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private BudgetRepository budgetRepository;

  @InjectMocks
  private BudgetLimitController budgetLimitController;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper = new ObjectMapper();
  private User user;
  private Budget budget;
  private BudgetLimit budgetLimit;
  private Category category;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper().findAndRegisterModules();
    mockMvc = MockMvcBuilders.standaloneSetup(budgetLimitController).build();
    user = new User(1L, "user@example.com", "testUser", "password", "Test User", null);
    budget = new Budget(1L, user, null);
    category = new Category(user, "Travel");
    category.setId(1L);

    budgetLimit = new BudgetLimit(1L, budget, category, BigDecimal.valueOf(300));
  }

  @Test
  void createBudgetLimit_ValidData_ReturnsCreatedLimit() throws Exception {
    CreateBudgetLimitDto createDto = new CreateBudgetLimitDto();
    createDto.setCategoryId(category.getId());
    createDto.setLimitAmount(BigDecimal.valueOf(300));

    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
    when(budgetRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(budget));
    when(budgetLimitService.createBudgetLimit(any(CreateBudgetLimitDto.class), eq(budget))).thenReturn(budgetLimit);

    mockMvc.perform(post("/api/user/budgetlimits/{budgetId}", budget.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createDto))
        .principal(() -> "testUser"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(budgetLimit.getId()));
  }

  @Test
  void getBudgetLimitById_ExistingId_ReturnsLimit() throws Exception {
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
    when(budgetLimitService.findBudgetLimitByIdAndUser(1L, user)).thenReturn(Optional.of(budgetLimit));

    mockMvc.perform(get("/api/user/budgetlimits/{id}", 1L)
        .principal(() -> "testUser"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(budgetLimit.getId()));
  }

  @Test
  void updateBudgetLimit_ExistingLimit_UpdatesAndReturnsUpdatedLimit() throws Exception {
    UpdateBudgetLimitDto updateDto = new UpdateBudgetLimitDto();
    updateDto.setBudgetId(budget.getId());
    updateDto.setCategoryId(category.getId());
    updateDto.setLimitAmount(BigDecimal.valueOf(350));

    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
    when(budgetLimitService.updateBudgetLimit(eq(1L), any(UpdateBudgetLimitDto.class), eq(user)))
      .thenReturn(Optional.of(budgetLimit));

    mockMvc.perform(put("/api/user/budgetlimits/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateDto))
        .principal(() -> "testUser"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.limitAmount").value(300));
  }

  @Test
  void deleteBudgetLimit_ExistingId_DeletesLimit() throws Exception {
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
    when(budgetLimitService.deleteBudgetLimit(1L, user)).thenReturn(true);

    mockMvc.perform(delete("/api/user/budgetlimits/{id}", 1L)
        .principal(() -> "testUser"))
      .andExpect(status().isOk());
  }
}
