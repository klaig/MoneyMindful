package io.github.kevinlaig.backend.service;

import io.github.kevinlaig.backend.dto.UpdateBudgetDto;
import io.github.kevinlaig.backend.model.Budget;
import io.github.kevinlaig.backend.model.BudgetLimit;
import io.github.kevinlaig.backend.model.Category;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.BudgetRepository;
import io.github.kevinlaig.backend.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for budget service.
 */
@ExtendWith(MockitoExtension.class)
public class BudgetServiceTest {

  @Mock
  private BudgetRepository budgetRepository;

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private BudgetService budgetService;

  private User testUser;
  private Budget testBudget;

  @BeforeEach
  void setUp() {
    testUser = new User("test@example.com", "testUser", "password", "Test User", null);
    testBudget = new Budget();
    testBudget.setUser(testUser);
    testBudget.setBudgetLimits(new HashSet<>());
  }

  @Test
  void createBudget_ValidBudget_ReturnsBudget() {
    when(budgetRepository.save(any(Budget.class))).thenReturn(testBudget);

    Budget result = budgetService.createBudget(testBudget, testUser);

    assertNotNull(result);
    assertEquals(testBudget, result);
  }

  @Test
  void findBudgetByIdAndUser_ValidIdAndUser_ReturnsBudget() {
    when(budgetRepository.findByIdAndUser(testBudget.getId(), testUser)).thenReturn(Optional.of(testBudget));

    Optional<Budget> result = budgetService.findBudgetByIdAndUser(testBudget.getId(), testUser);

    assertTrue(result.isPresent());
    assertEquals(testBudget, result.get());
  }

  @Test
  void getAllUserBudgets_ValidUser_ReturnsBudgetsList() {
    List<Budget> budgets = Collections.singletonList(testBudget);
    when(budgetRepository.findByUser(testUser)).thenReturn(budgets);

    List<Budget> result = budgetService.getAllUserBudgets(testUser);

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
  }

  @Test
  void updateBudget_ExistingBudget_UpdatesAndReturnsBudget() {
    Long categoryId = 1L;
    BigDecimal newLimit = new BigDecimal("500.00");
    UpdateBudgetDto updateDto = new UpdateBudgetDto();
    Map<Long, BigDecimal> updatedLimits = new HashMap<>();
    updatedLimits.put(categoryId, newLimit);
    updateDto.setBudgetLimits(updatedLimits);

    Category category = new Category(testUser, "TestCategory");
    BudgetLimit updatedBudgetLimit = new BudgetLimit(null, testBudget, category, newLimit);

    when(budgetRepository.findByIdAndUser(testBudget.getId(), testUser)).thenReturn(Optional.of(testBudget));
    when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
    when(budgetRepository.save(any(Budget.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Optional<Budget> result = budgetService.updateBudget(testBudget.getId(), updateDto, testUser);

    assertTrue(result.isPresent());
    Budget updatedBudget = result.get();
    assertNotNull(updatedBudget.getBudgetLimits());
    assertFalse(updatedBudget.getBudgetLimits().isEmpty());
    assertTrue(updatedBudget.getBudgetLimits().contains(updatedBudgetLimit));
  }


  @Test
  void deleteBudget_ExistingBudget_DeletesAndReturnsTrue() {
    when(budgetRepository.findByIdAndUser(testBudget.getId(), testUser)).thenReturn(Optional.of(testBudget));

    boolean result = budgetService.deleteBudget(testBudget.getId(), testUser);

    assertTrue(result);
  }

  @Test
  void deleteBudget_NonExistingBudget_ReturnsFalse() {
    when(budgetRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.empty());

    boolean result = budgetService.deleteBudget(1L, testUser);

    assertFalse(result);
  }
}
