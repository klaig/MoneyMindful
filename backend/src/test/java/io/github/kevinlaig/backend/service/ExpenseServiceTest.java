package io.github.kevinlaig.backend.service;

import io.github.kevinlaig.backend.model.Category;
import io.github.kevinlaig.backend.model.Role;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import io.github.kevinlaig.backend.dto.UpdateExpenseDto;
import io.github.kevinlaig.backend.model.Expense;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.CategoryRepository;
import io.github.kevinlaig.backend.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for expense service.
 */
@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {

  @Mock
  private ExpenseRepository expenseRepository;

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private ExpenseService expenseService;

  private User testUser;
  private Expense testExpense;
  private UpdateExpenseDto updateExpenseDto;

  @BeforeEach
  void setUp() {
    testUser = new User("test@example.com", "testUser", "password", "Test User", new Role());
    Category testCategory = new Category(testUser, "Test Category");
    testExpense = new Expense(testUser, BigDecimal.valueOf(100.00), testCategory, LocalDateTime.now(), "Test notes");
    updateExpenseDto = new UpdateExpenseDto();
    updateExpenseDto.setAmount(BigDecimal.valueOf(200.00));
    updateExpenseDto.setCategoryId(2L);
    updateExpenseDto.setDateTime(LocalDateTime.now());
    updateExpenseDto.setNotes("Updated notes");
  }

  @Test
  void createExpense_ValidExpense_ReturnsExpense() {
    when(expenseRepository.save(any(Expense.class))).thenReturn(testExpense);

    Expense result = expenseService.createExpense(testExpense);

    assertNotNull(result);
    assertEquals(testExpense, result);
  }

  @Test
  void getAllUserExpenses_ValidUser_ReturnsExpensesList() {
    when(expenseRepository.findByUser(testUser)).thenReturn(Collections.singletonList(testExpense));

    List<Expense> result = expenseService.getAllUserExpenses(testUser);

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(testExpense, result.getFirst());
  }

  @Test
  void getAllUserExpenses_UserWithNoExpenses_ReturnsEmptyList() {
    User existingUser = new User("existing@example.com", "existingUser", "password", "Existing User", new Role());
    when(expenseRepository.findByUser(existingUser)).thenReturn(Collections.emptyList());

    List<Expense> result = expenseService.getAllUserExpenses(existingUser);

    assertTrue(result.isEmpty());
  }

  @Test
  void findExpenseByIdAndUser_ValidIdAndUser_ReturnsExpense() {
    when(expenseRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.of(testExpense));

    Optional<Expense> result = expenseService.findExpenseByIdAndUser(1L, testUser);

    assertTrue(result.isPresent());
    assertEquals(testExpense, result.get());
  }

  @Test
  void updateExpense_ExistingExpense_UpdatesAndReturnsExpense() {
    when(expenseRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.of(testExpense));
    when(expenseRepository.save(any(Expense.class))).thenReturn(testExpense);
    when(categoryRepository.findById(updateExpenseDto.getCategoryId())).thenReturn(Optional.of(new Category()));

    Optional<Expense> result = expenseService.updateExpense(1L, updateExpenseDto, testUser);

    assertTrue(result.isPresent());
    assertEquals(BigDecimal.valueOf(200.00), result.get().getAmount());
    assertEquals("Updated notes", result.get().getNotes());
  }

  @Test
  void updateExpense_NonExistingExpense_ReturnsEmptyOptional() {
    when(expenseRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.empty());

    Optional<Expense> result = expenseService.updateExpense(1L, updateExpenseDto, testUser);

    assertTrue(result.isEmpty());
  }

  @Test
  void deleteExpense_ValidIdAndUser_DeletesAndReturnsTrue() {
    when(expenseRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.of(testExpense));

    boolean result = expenseService.deleteExpense(1L, testUser);

    assertTrue(result);
  }

  @Test
  void deleteExpense_NonExistingExpense_ReturnsFalse() {
    when(expenseRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.empty());

    boolean result = expenseService.deleteExpense(1L, testUser);

    assertFalse(result);
  }

}
