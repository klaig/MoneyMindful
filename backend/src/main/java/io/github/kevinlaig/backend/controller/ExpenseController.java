package io.github.kevinlaig.backend.controller;

import io.github.kevinlaig.backend.model.Expense;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.UserRepository;
import io.github.kevinlaig.backend.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  // Create an Expense
  @PostMapping
  public ResponseEntity<Expense> createExpense(Expense expense) {
    return ResponseEntity.ok(expenseService.createExpense(expense));
  }

  // Get all Expenses for the authenticated user
  @GetMapping
  public ResponseEntity<List<Expense>> getAllExpenses(Principal principal) {
    Optional<User> userOptional = userRepository.findByUsername(principal.getName());
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      List<Expense> expenses = expenseService.findAllUserExpenses(user);
      return ResponseEntity.ok(expenses);
    } else {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

  }

  // Get an Expense by ID
  @GetMapping("/{id}")
  public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
    Optional<Expense> expense = expenseService.findExpenseById(id);
    return expense.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  // Update an Expense
  @PutMapping("/{id}")
  public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
    Optional<Expense> expenseData = expenseService.findExpenseById(id);

    if (expenseData.isPresent()) {
      Expense updatedExpense = expenseData.get();
      updatedExpense.setAmount(expense.getAmount());
      updatedExpense.setDateTime(expense.getDateTime());
      updatedExpense.setCategory(expense.getCategory());
      updatedExpense.setNotes(expense.getNotes());
      return ResponseEntity.ok(expenseService.updateExpense(updatedExpense));
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  // Delete an Expense
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteExpense(@PathVariable Long id) {
    Optional<Expense> expense = expenseService.findExpenseById(id);

    if (expense.isPresent()) {
      expenseService.deleteExpense(id);
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
