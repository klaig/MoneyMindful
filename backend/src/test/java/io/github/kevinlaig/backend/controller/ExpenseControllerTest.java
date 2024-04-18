package io.github.kevinlaig.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kevinlaig.backend.dto.CreateExpenseDto;
import io.github.kevinlaig.backend.dto.UpdateExpenseDto;
import io.github.kevinlaig.backend.model.Category;
import io.github.kevinlaig.backend.model.Expense;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.UserRepository;
import io.github.kevinlaig.backend.service.ExpenseService;
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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseControllerTest {

  @Mock
  private ExpenseService expenseService;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private ExpenseController expenseController;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper = new ObjectMapper();

  private User user;
  private Expense expense;
  private CreateExpenseDto createExpenseDto;
  private UpdateExpenseDto updateExpenseDto;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper().findAndRegisterModules();
    mockMvc = MockMvcBuilders.standaloneSetup(expenseController).build();
    user = new User(1L, "user@example.com", "testUser", "password", "Test User", null);
    expense = new Expense(user, BigDecimal.valueOf(100.00), new Category(), LocalDateTime.now(), "Lunch");

    createExpenseDto = new CreateExpenseDto();
    createExpenseDto.setAmount(BigDecimal.valueOf(100.00));
    createExpenseDto.setCategoryId(1L);
    createExpenseDto.setDateTime(LocalDateTime.now());
    createExpenseDto.setNotes("Lunch at restaurant");

    updateExpenseDto = new UpdateExpenseDto();
    updateExpenseDto.setAmount(BigDecimal.valueOf(120.00));
    updateExpenseDto.setCategoryId(1L);
    updateExpenseDto.setDateTime(LocalDateTime.now());
    updateExpenseDto.setNotes("Updated lunch");
  }

  @Test
  void createExpense_ValidData_ReturnsCreatedExpense() throws Exception {
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
    when(expenseService.createExpense(any(CreateExpenseDto.class), eq(user))).thenReturn(expense);

    mockMvc.perform(post("/api/user/expenses")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createExpenseDto))
        .principal(() -> "testUser"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.notes").value("Lunch"));
  }

  @Test
  void getAllUserExpenses_ReturnsExpensesList() throws Exception {
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
    when(expenseService.getAllUserExpenses(user)).thenReturn(Collections.singletonList(expense));

    mockMvc.perform(get("/api/user/expenses")
        .principal(() -> "testUser"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].notes").value("Lunch"));
  }

  @Test
  void getExpenseByIdAndUser_ExistingId_ReturnsExpense() throws Exception {
    Long expenseId = 1L;
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
    when(expenseService.findExpenseByIdAndUser(expenseId, user)).thenReturn(Optional.of(expense));

    mockMvc.perform(get("/api/user/expenses/{id}", expenseId)
        .principal(() -> "testUser"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.notes").value("Lunch"));
  }

  @Test
  void updateExpense_ExistingExpense_UpdatesAndReturnsUpdatedExpense() throws Exception {
    Long expenseId = 1L;

    Expense updatedExpense = new Expense();
    updatedExpense.setId(expenseId);
    updatedExpense.setUser(user);
    updatedExpense.setAmount(updateExpenseDto.getAmount());
    updatedExpense.setNotes(updateExpenseDto.getNotes());
    updatedExpense.setDateTime(updateExpenseDto.getDateTime());

    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
    when(expenseService.updateExpense(eq(expenseId), any(UpdateExpenseDto.class), eq(user)))
      .thenReturn(Optional.of(updatedExpense));

    mockMvc.perform(put("/api/user/expenses/{id}", expenseId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateExpenseDto))
        .principal(() -> "testUser"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.amount").value(120.00))
      .andExpect(jsonPath("$.notes").value("Updated lunch"));
  }


  @Test
  void deleteExpense_ExistingId_DeletesExpense() throws Exception {
    Long expenseId = 1L;
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
    when(expenseService.deleteExpense(expenseId, user)).thenReturn(true);

    mockMvc.perform(delete("/api/user/expenses/{id}", expenseId)
        .principal(() -> "testUser"))
      .andExpect(status().isOk());
  }
}
