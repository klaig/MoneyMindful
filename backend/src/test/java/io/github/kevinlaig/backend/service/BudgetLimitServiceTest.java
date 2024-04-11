package io.github.kevinlaig.backend.service;

import io.github.kevinlaig.backend.dto.CreateBudgetLimitDto;
import io.github.kevinlaig.backend.dto.UpdateBudgetLimitDto;
import io.github.kevinlaig.backend.mapper.BudgetLimitMapper;
import io.github.kevinlaig.backend.model.Budget;
import io.github.kevinlaig.backend.model.BudgetLimit;
import io.github.kevinlaig.backend.model.Category;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.BudgetLimitRepository;
import io.github.kevinlaig.backend.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for budget limit service.
 */
@ExtendWith(MockitoExtension.class)
public class BudgetLimitServiceTest {

  @Mock
  private BudgetLimitRepository budgetLimitRepository;

  @Mock
  private CategoryRepository categoryRepository;

  @Mock
  private BudgetLimitMapper budgetLimitMapper;

  @InjectMocks
  private BudgetLimitService budgetLimitService;

  private User testUser;
  private Budget testBudget;
  private BudgetLimit testBudgetLimit;
  private CreateBudgetLimitDto createBudgetLimitDto;
  private UpdateBudgetLimitDto updateBudgetLimitDto;
  private Category testCategory;

  @BeforeEach
  void setUp() {
    testUser = new User("test@example.com", "testUser", "password", "Test User", null);
    testBudget = new Budget();
    testBudget.setUser(testUser);
    testBudget.setBudgetLimits(new HashSet<>());

    testCategory = new Category(testUser, "TestCategory");
    testBudgetLimit = new BudgetLimit(1L, testBudget, testCategory, BigDecimal.valueOf(100.00));

    createBudgetLimitDto = new CreateBudgetLimitDto();
    createBudgetLimitDto.setCategoryId(testCategory.getId());
    createBudgetLimitDto.setLimitAmount(BigDecimal.valueOf(100.00));

    updateBudgetLimitDto = new UpdateBudgetLimitDto();
    updateBudgetLimitDto.setBudgetId(testBudget.getId());
    updateBudgetLimitDto.setCategoryId(testCategory.getId());
    updateBudgetLimitDto.setLimitAmount(BigDecimal.valueOf(200.00));
  }

  @Test
  void createBudgetLimit_ValidData_ReturnsBudgetLimit() {
    when(budgetLimitMapper.toEntity(any(CreateBudgetLimitDto.class))).thenReturn(testBudgetLimit);
    when(budgetLimitRepository.save(any(BudgetLimit.class))).thenReturn(testBudgetLimit);

    BudgetLimit result = budgetLimitService.createBudgetLimit(createBudgetLimitDto, testBudget);

    assertNotNull(result);
    assertEquals(testBudgetLimit, result);
    verify(budgetLimitMapper).toEntity(any(CreateBudgetLimitDto.class));
    verify(budgetLimitRepository).save(any(BudgetLimit.class));
  }

  @Test
  void findBudgetLimitByIdAndUser_ValidIdAndUser_ReturnsBudgetLimit() {
    when(budgetLimitRepository.findByIdAndUser(testBudgetLimit.getId(), testUser)).thenReturn(Optional.of(testBudgetLimit));

    Optional<BudgetLimit> result = budgetLimitService.findBudgetLimitByIdAndUser(testBudgetLimit.getId(), testUser);

    assertTrue(result.isPresent());
    assertEquals(testBudgetLimit, result.get());
  }

  @Test
  void updateBudgetLimit_ExistingBudgetLimit_UpdatesAndReturnsBudgetLimit() {
    when(budgetLimitRepository.findByIdAndUser(testBudgetLimit.getId(), testUser)).thenReturn(Optional.of(testBudgetLimit));
    when(categoryRepository.findById(updateBudgetLimitDto.getCategoryId())).thenReturn(Optional.of(testCategory));
    when(budgetLimitRepository.save(any(BudgetLimit.class))).thenReturn(testBudgetLimit);

    Optional<BudgetLimit> result = budgetLimitService.updateBudgetLimit(testBudgetLimit.getId(), updateBudgetLimitDto, testUser);

    assertTrue(result.isPresent());
    assertEquals(testBudgetLimit, result.get());
    assertEquals(updateBudgetLimitDto.getLimitAmount(), result.get().getLimitAmount());
  }

  @Test
  void deleteBudgetLimit_ExistingBudgetLimit_DeletesAndReturnsTrue() {
    when(budgetLimitRepository.findByIdAndUser(testBudgetLimit.getId(), testUser)).thenReturn(Optional.of(testBudgetLimit));
    doNothing().when(budgetLimitRepository).deleteById(testBudgetLimit.getId());

    boolean result = budgetLimitService.deleteBudgetLimit(testBudgetLimit.getId(), testUser);

    assertTrue(result);
    verify(budgetLimitRepository).deleteById(testBudgetLimit.getId());
  }

  @Test
  void deleteBudgetLimit_NonExistingBudgetLimit_ReturnsFalse() {
    when(budgetLimitRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.empty());

    boolean result = budgetLimitService.deleteBudgetLimit(1L, testUser);

    assertFalse(result);
  }

}

