package io.github.kevinlaig.backend.service;

import io.github.kevinlaig.backend.dto.CreateBudgetLimitDto;
import io.github.kevinlaig.backend.dto.UpdateBudgetLimitDto;
import io.github.kevinlaig.backend.mapper.BudgetLimitMapper;
import io.github.kevinlaig.backend.model.Budget;
import io.github.kevinlaig.backend.model.BudgetLimit;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.BudgetLimitRepository;
import io.github.kevinlaig.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service for budget limits.
 */
@Service
public class BudgetLimitService {

  private final BudgetLimitRepository budgetLimitRepository;
  private final CategoryRepository categoryRepository;
  private final BudgetLimitMapper budgetLimitMapper;

  @Autowired
  public BudgetLimitService(BudgetLimitRepository budgetLimitRepository, CategoryRepository categoryRepository, BudgetLimitMapper budgetLimitMapper) {
    this.budgetLimitRepository = budgetLimitRepository;
    this.categoryRepository = categoryRepository;
    this.budgetLimitMapper = budgetLimitMapper;
  }

  /**
   * Create a BudgetLimit.
   *
   * @param createBudgetLimitDto CreateBudgetLimitDto
   * @param budget               Budget
   * @return the created BudgetLimit
   */
  @Transactional
  @PreAuthorize("#budget.user.username == authentication.principal.username")
  public BudgetLimit createBudgetLimit(CreateBudgetLimitDto createBudgetLimitDto, Budget budget) {
    BudgetLimit budgetLimit = budgetLimitMapper.toEntity(createBudgetLimitDto);
    budgetLimit.setBudget(budget); // Set the associated budget
    return budgetLimitRepository.save(budgetLimit);
  }

  /**
   * Find BudgetLimit by id and user.
   *
   * @param id   Long
   * @param user User
   * @return Optional of BudgetLimit
   */
  @PreAuthorize("#user.username == authentication.principal.username")
  public Optional<BudgetLimit> findBudgetLimitByIdAndUser(Long id, User user) {
    return budgetLimitRepository.findByIdAndUser(id, user);
  }

  /**
   * Update a BudgetLimit.
   *
   * @param id       Long
   * @param updateDto UpdateBudgetLimitDto
   * @param user     User
   * @return Optional of updated BudgetLimit
   */
  @Transactional
  @PreAuthorize("#user.username == authentication.principal.username")
  public Optional<BudgetLimit> updateBudgetLimit(Long id, UpdateBudgetLimitDto updateDto, User user) {
    return budgetLimitRepository.findByIdAndUser(id, user).map(budgetLimit -> {
      categoryRepository.findById(updateDto.getCategoryId()).ifPresent(budgetLimit::setCategory);
      budgetLimit.setLimitAmount(updateDto.getLimitAmount());
      return budgetLimitRepository.save(budgetLimit);
    });
  }

  /**
   * Delete a BudgetLimit.
   *
   * @param id   Long
   * @param user User
   * @return true if deleted, false otherwise
   */
  @Transactional
  @PreAuthorize("#user.username == authentication.principal.username")
  public boolean deleteBudgetLimit(Long id, User user) {
    Optional<BudgetLimit> existingLimit = budgetLimitRepository.findByIdAndUser(id, user);
    if (existingLimit.isPresent()) {
      budgetLimitRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
