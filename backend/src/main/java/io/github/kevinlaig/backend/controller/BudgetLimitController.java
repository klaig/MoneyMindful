package io.github.kevinlaig.backend.controller;

import io.github.kevinlaig.backend.dto.CreateBudgetLimitDto;
import io.github.kevinlaig.backend.dto.UpdateBudgetLimitDto;
import io.github.kevinlaig.backend.model.Budget;
import io.github.kevinlaig.backend.model.BudgetLimit;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.BudgetRepository;
import io.github.kevinlaig.backend.repository.UserRepository;
import io.github.kevinlaig.backend.service.BudgetLimitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

/**
 * Controller for managing budget limits.
 */
@RestController
@RequestMapping("/api/user/budgetlimits")
public class BudgetLimitController {

  private final BudgetLimitService budgetLimitService;
  private final UserRepository userRepository;
  private final BudgetRepository budgetRepository;

  @Autowired
  public BudgetLimitController(BudgetLimitService budgetLimitService, UserRepository userRepository, BudgetRepository budgetRepository) {
    this.budgetLimitService = budgetLimitService;
    this.userRepository = userRepository;
    this.budgetRepository = budgetRepository;
  }

  /**
   * Create a new budget limit for a given budget.
   *
   * @param budgetId ID of the budget to which the limit is associated.
   * @param createBudgetLimitDto DTO for creating a budget limit.
   * @param principal Principal object to extract user information.
   * @return ResponseEntity with the created BudgetLimit.
   */
  @PostMapping("/{budgetId}")
  public ResponseEntity<BudgetLimit> createBudgetLimit(@PathVariable Long budgetId, @RequestBody @Valid CreateBudgetLimitDto createBudgetLimitDto, Principal principal) {
    User user = userRepository.findByUsername(principal.getName())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    Optional<Budget> budget = budgetRepository.findByIdAndUser(budgetId, user);
    if (budget.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    BudgetLimit createdLimit = budgetLimitService.createBudgetLimit(createBudgetLimitDto, budget.get());
    return ResponseEntity.ok(createdLimit);
  }

  /**
   * Get a budget limit by its ID.
   *
   * @param id The ID of the budget limit.
   * @param principal Principal object for user identification.
   * @return ResponseEntity containing the budget limit or Not Found.
   */
  @GetMapping("/{id}")
  public ResponseEntity<BudgetLimit> getBudgetLimitById(@PathVariable Long id, Principal principal) {
    User user = userRepository.findByUsername(principal.getName())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    Optional<BudgetLimit> budgetLimit = budgetLimitService.findBudgetLimitByIdAndUser(id, user);
    return budgetLimit.map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Update a budget limit.
   *
   * @param id The ID of the budget limit to update.
   * @param updateDto DTO containing the updated details of the budget limit.
   * @param principal Principal object for user identification.
   * @return ResponseEntity containing the updated budget limit or Not Found.
   */
  @PutMapping("/{id}")
  public ResponseEntity<BudgetLimit> updateBudgetLimit(@PathVariable Long id, @RequestBody @Valid UpdateBudgetLimitDto updateDto, Principal principal) {
    User user = userRepository.findByUsername(principal.getName())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    Optional<BudgetLimit> updatedLimit = budgetLimitService.updateBudgetLimit(id, updateDto, user);
    return updatedLimit.map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Delete a budget limit by its ID.
   *
   * @param id The ID of the budget limit to delete.
   * @param principal Principal object for user authentication.
   * @return ResponseEntity indicating the success or failure of the delete operation.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteBudgetLimit(@PathVariable Long id, Principal principal) {
    User user = userRepository.findByUsername(principal.getName())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    boolean isDeleted = budgetLimitService.deleteBudgetLimit(id, user);
    if (isDeleted) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
