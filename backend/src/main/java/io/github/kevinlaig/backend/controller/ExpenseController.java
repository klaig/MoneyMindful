package io.github.kevinlaig.backend.controller;

import io.github.kevinlaig.backend.dto.UpdateExpenseDto;
import io.github.kevinlaig.backend.model.Expense;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.UserRepository;
import io.github.kevinlaig.backend.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Controller for expenses.
 */
@RestController
@RequestMapping("/api/user/expenses")
public class ExpenseController {

  private final ExpenseService expenseService;
  private final UserRepository userRepository;

  @Autowired
  public ExpenseController(ExpenseService expenseService, UserRepository userRepository) {
    this.expenseService = expenseService;
    this.userRepository = userRepository;
  }

  /**
   * Create an Expense
   *
   * @param expense Expense
   * @return Created Expense
   */
  @PostMapping
  public ResponseEntity<Expense> createExpense(Expense expense) {
    return ResponseEntity.ok(expenseService.createExpense(expense));
  }

  /**
   * Get all Expenses for the authenticated user
   *
   * @param principal Principal
   * @return List of Expenses
   */
  @GetMapping
  public ResponseEntity<List<Expense>> getAllUserExpenses(Principal principal) {
    Optional<User> userOptional = userRepository.findByUsername(principal.getName());
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      List<Expense> expenses = expenseService.getAllUserExpenses(user);
      return ResponseEntity.ok(expenses);
    } else {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }
  }

  // Get an Expense by ID
  @GetMapping("/{id}")
  public ResponseEntity<Expense> getExpenseByIdAndUser(@PathVariable Long id, Principal principal) {
    String username = principal.getName();
    User user = userRepository.findByUsername(username)
                              .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    Optional<Expense> expense = expenseService.findExpenseByIdAndUser(id, user);
    return expense.map(ResponseEntity::ok)
                  .orElseGet(() -> ResponseEntity.notFound().build());
  }

  // Update an Expense
  @PutMapping("/{id}")
  public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @RequestBody UpdateExpenseDto expenseDetails, Principal principal) {
    String username = principal.getName();
    User user = userRepository.findByUsername(username)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    Optional<Expense> updatedExpense = expenseService.updateExpense(id, expenseDetails, user);
    return updatedExpense.map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  // Delete an Expense
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteExpense(@PathVariable Long id, Principal principal) {
    String username = principal.getName();
    User user = userRepository.findByUsername(username)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    boolean isDeleted = expenseService.deleteExpense(id, user);
    if (isDeleted) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
