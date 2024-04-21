package io.github.kevinlaig.backend.controller;

import io.github.kevinlaig.backend.dto.CreateBudgetDto;
import io.github.kevinlaig.backend.dto.UpdateBudgetDto;
import io.github.kevinlaig.backend.model.Budget;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.UserRepository;
import io.github.kevinlaig.backend.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Controller for managing budgets.
 */
@RestController
@RequestMapping("/api/user/budgets")
public class BudgetController {

  private final BudgetService budgetService;
  private final UserRepository userRepository;

  @Autowired
  public BudgetController(BudgetService budgetService, UserRepository userRepository) {
    this.budgetService = budgetService;
    this.userRepository = userRepository;
  }

  /**
   * Create a new budget.
   *
   * @param createBudgetDto DTO for creating a budget
   * @param principal       Principal for the authenticated user
   * @return ResponseEntity containing the created budget
   */
  @PostMapping
  public ResponseEntity<Budget> createBudget(@RequestBody @Valid CreateBudgetDto createBudgetDto, Principal principal) {
    User user = userRepository.findByUsername(principal.getName())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    Budget createdBudget = budgetService.createBudget(createBudgetDto, user);
    return ResponseEntity.ok(createdBudget);
  }

  /**
   * Get all budgets for the authenticated user.
   *
   * @param principal Principal of the authenticated user
   * @return ResponseEntity with list of budgets
   */
  @GetMapping
  public ResponseEntity<List<Budget>> getAllUserBudgets(Principal principal) {
    User user = userRepository.findByUsername(principal.getName())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    List<Budget> budgets = budgetService.getAllUserBudgets(user);
    return ResponseEntity.ok(budgets);
  }

  /**
   * Get a budget by its ID.
   *
   * @param id        ID of the budget
   * @param principal Principal of the authenticated user
   * @return ResponseEntity with the requested budget
   */
  @GetMapping("/{id}")
  public ResponseEntity<Budget> getBudgetById(@PathVariable Long id, Principal principal) {
    User user = userRepository.findByUsername(principal.getName())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    Optional<Budget> budget = budgetService.findBudgetByIdAndUser(id, user);
    return budget.map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Update a budget.
   *
   * @param id          ID of the budget to update
   * @param updateDto   DTO containing updated budget details
   * @param principal   Principal of the authenticated user
   * @return ResponseEntity with the updated budget
   */
  @PutMapping("/{id}")
  public ResponseEntity<Budget> updateBudget(@PathVariable Long id, @RequestBody @Valid UpdateBudgetDto updateDto, Principal principal) {
    User user = userRepository.findByUsername(principal.getName())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    Optional<Budget> updatedBudget = budgetService.updateBudget(id, updateDto, user);
    return updatedBudget.map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Delete a budget.
   *
   * @param id        ID of the budget to delete
   * @param principal Principal of the authenticated user
   * @return ResponseEntity indicating success or failure
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteBudget(@PathVariable Long id, Principal principal) {
    User user = userRepository.findByUsername(principal.getName())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    boolean isDeleted = budgetService.deleteBudget(id, user);
    if (isDeleted) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
