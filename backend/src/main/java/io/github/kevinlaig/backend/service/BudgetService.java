package io.github.kevinlaig.backend.service;

import io.github.kevinlaig.backend.dto.CreateBudgetDto;
import io.github.kevinlaig.backend.dto.UpdateBudgetDto;
import io.github.kevinlaig.backend.mapper.BudgetMapper;
import io.github.kevinlaig.backend.model.Budget;
import io.github.kevinlaig.backend.model.BudgetLimit;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.BudgetRepository;
import io.github.kevinlaig.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for budget operations.
 */
@Service
public class BudgetService {

  private final BudgetRepository budgetRepository;
  private final CategoryRepository categoryRepository;
  private final BudgetMapper budgetMapper;

  @Autowired
  public BudgetService(BudgetRepository budgetRepository, CategoryRepository categoryRepository, BudgetMapper budgetMapper) {
    this.budgetRepository = budgetRepository;
    this.categoryRepository = categoryRepository;
    this.budgetMapper = budgetMapper;
  }

  /**
   * Create a budget.
   *
   * @param createBudgetDto CreateBudgetDto
   * @param user            User
   * @return the created budget
   */
  @Transactional
  @PreAuthorize("#user.username == authentication.principal.username")
  public Budget createBudget(CreateBudgetDto createBudgetDto, User user) {
    Budget budget = budgetMapper.toEntity(createBudgetDto);
    budget.setUser(user);
    budget.getBudgetLimits().forEach(limit -> limit.setBudget(budget));
    return budgetRepository.save(budget);
  }

  /**
   * Find a budget by ID and user.
   *
   * @param id   Budget ID
   * @param user User
   * @return Optional of budget
   */
  @PreAuthorize("#user.username == authentication.principal.username")
  public Optional<Budget> findBudgetByIdAndUser(Long id, User user) {
    return budgetRepository.findByIdAndUser(id, user);
  }

  /**
   * Get all budgets for a user.
   *
   * @param user User
   * @return List of budgets
   */
  @PreAuthorize("#user.username == authentication.principal.username")
  public List<Budget> getAllUserBudgets(User user) {
    return budgetRepository.findByUser(user);
  }

  /**
   * Update a budget.
   *
   * @param budgetId  Budget ID
   * @param updateDto DTO containing updated budget details
   * @param user      User
   * @return the updated budget
   */
  @Transactional
  @PreAuthorize("#user.username == authentication.principal.username")
  public Optional<Budget> updateBudget(Long budgetId, UpdateBudgetDto updateDto, User user) {
    return budgetRepository.findByIdAndUser(budgetId, user).map(budget -> {
      Set<BudgetLimit> updatedLimits = updateDto.getBudgetLimits().entrySet().stream()
        .map(entry -> new BudgetLimit(null, budget, categoryRepository.findById(entry.getKey()).orElse(null), entry.getValue()))
        .collect(Collectors.toSet());
      budget.setBudgetLimits(updatedLimits);
      return budgetRepository.save(budget);
    });
  }

  /**
   * Delete a budget.
   *
   * @param id Budget ID
   * @param user     User
   */
  @Transactional
  @PreAuthorize("#user.username == authentication.principal.username")
  public boolean deleteBudget(Long id, User user) {
    Optional<Budget> existingBudget = budgetRepository.findByIdAndUser(id, user);
    if (existingBudget.isPresent()) {
      budgetRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
