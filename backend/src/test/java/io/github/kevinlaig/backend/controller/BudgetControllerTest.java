package io.github.kevinlaig.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kevinlaig.backend.dto.CreateBudgetDto;
import io.github.kevinlaig.backend.dto.CreateBudgetLimitDto;
import io.github.kevinlaig.backend.dto.UpdateBudgetDto;
import io.github.kevinlaig.backend.model.Budget;
import io.github.kevinlaig.backend.model.BudgetLimit;
import io.github.kevinlaig.backend.model.Category;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.UserRepository;
import io.github.kevinlaig.backend.service.BudgetService;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BudgetControllerTest {

  @Mock
  private BudgetService budgetService;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private BudgetController budgetController;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper = new ObjectMapper();
  private User user;
  private Budget budget;
  private Set<BudgetLimit> budgetLimits;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper().findAndRegisterModules();
    mockMvc = MockMvcBuilders.standaloneSetup(budgetController).build();
    user = new User(1L, "user@example.com", "testUser", "password", "Test User", null);
    budget = new Budget();
    budget.setId(1L);
    budget.setUser(user);

    budgetLimits = new HashSet<>();
    BudgetLimit limit = new BudgetLimit();
    limit.setCategory(new Category(user, "Travel"));
    limit.setLimitAmount(BigDecimal.valueOf(500));
    budgetLimits.add(limit);
    budget.setBudgetLimits(budgetLimits);
  }

  @Test
  void createBudget_ValidData_ReturnsCreatedBudget() throws Exception {
    CreateBudgetDto createDto = new CreateBudgetDto();
    CreateBudgetLimitDto limitDto = new CreateBudgetLimitDto();
    limitDto.setCategoryId(1L);
    limitDto.setLimitAmount(BigDecimal.valueOf(500));
    createDto.setBudgetLimits(Collections.singleton(limitDto));

    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
    when(budgetService.createBudget(any(CreateBudgetDto.class), eq(user))).thenReturn(budget);

    mockMvc.perform(post("/api/user/budgets")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createDto))
        .principal(() -> "testUser"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(budget.getId()));
  }

  @Test
  void getAllUserBudgets_ReturnsBudgetsList() throws Exception {
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
    when(budgetService.getAllUserBudgets(user)).thenReturn(Collections.singletonList(budget));

    mockMvc.perform(get("/api/user/budgets")
        .principal(() -> "testUser"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].id").value(budget.getId()));
  }

  @Test
  void getBudgetById_ExistingId_ReturnsBudget() throws Exception {
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
    when(budgetService.findBudgetByIdAndUser(1L, user)).thenReturn(Optional.of(budget));

    mockMvc.perform(get("/api/user/budgets/{id}", 1)
        .principal(() -> "testUser"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(budget.getId()));
  }

  @Test
  void updateBudget_ExistingBudget_UpdatesAndReturnsUpdatedBudget() throws Exception {
    Long budgetId = 1L;
    UpdateBudgetDto updateDto = new UpdateBudgetDto();
    updateDto.setBudgetLimits(Collections.singletonMap(1L, BigDecimal.valueOf(600)));

    Budget updatedBudget = new Budget();
    updatedBudget.setId(budgetId);
    updatedBudget.setUser(user);
    updatedBudget.setBudgetLimits(budgetLimits);

    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
    when(budgetService.updateBudget(eq(budgetId), any(UpdateBudgetDto.class), eq(user)))
      .thenReturn(Optional.of(updatedBudget));

    mockMvc.perform(put("/api/user/budgets/{id}", budgetId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateDto))
        .principal(() -> "testUser"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(budgetId));
  }

  @Test
  void deleteBudget_ExistingId_DeletesBudget() throws Exception {
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
    when(budgetService.deleteBudget(1L, user)).thenReturn(true);

    mockMvc.perform(delete("/api/user/budgets/{id}", 1)
        .principal(() -> "testUser"))
      .andExpect(status().isOk());
  }
}
