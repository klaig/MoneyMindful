package io.github.kevinlaig.backend.service;

import io.github.kevinlaig.backend.dto.UpdateBudgetLimitDto;
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

  @Autowired
  public BudgetLimitService(BudgetLimitRepository budgetLimitRepository, CategoryRepository categoryRepository) {
    this.budgetLimitRepository = budgetLimitRepository;
    this.categoryRepository = categoryRepository;
  }

  /**
   * Create a BudgetLimit.
   *
   * @param budgetLimit BudgetLimit
   * @param user        User
   * @return Created BudgetLimit
   */
  @Transactional
  @PreAuthorize("#user.username == authentication.principal.username")
  public BudgetLimit createBudgetLimit(BudgetLimit budgetLimit, User user) {
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
