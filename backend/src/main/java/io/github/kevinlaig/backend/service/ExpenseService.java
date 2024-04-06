package io.github.kevinlaig.backend.service;

import io.github.kevinlaig.backend.model.Expense;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for expenses.
 */
@Service
public class ExpenseService {

  private final ExpenseRepository expenseRepository;

  @Autowired
  public ExpenseService(ExpenseRepository expenseRepository) {
    this.expenseRepository = expenseRepository;
  }

  // Create an Expense
  @PreAuthorize("#expense.user.username == authentication.principal.username")
  public Expense createExpense(Expense expense) {
    return expenseRepository.save(expense);
  }

  // Get all Expenses for the authenticated user
  @PreAuthorize("#user.username == authentication.principal.username")
  public List<Expense> findAllUserExpenses(User user) {
    return expenseRepository.findByUser(user);
  }

  // Get an Expense by ID
  @PreAuthorize("#user.username == authentication.principal.username")
  public Optional<Expense> findExpenseByIdAndUser(Long id, User user) {
    return expenseRepository.findByIdAndUser(id, user);
  }

  // Update an Expense
  @PreAuthorize("#user.username == authentication.principal.username")
  public Optional<Expense> updateExpense(Long id, Expense expenseDetails, User user) {
    Optional<Expense> existingExpense = expenseRepository.findByIdAndUser(id, user);
    if (existingExpense.isPresent()) {
      Expense updatedExpense = existingExpense.get();
      updatedExpense.setAmount(expenseDetails.getAmount());
      updatedExpense.setCategory(expenseDetails.getCategory());
      updatedExpense.setDateTime(expenseDetails.getDateTime());
      updatedExpense.setNotes(expenseDetails.getNotes());
      return Optional.of(expenseRepository.save(updatedExpense));
    }
    return Optional.empty();
  }

  // Delete an Expense
  @PreAuthorize("#user.username == authentication.principal.username")
  public boolean deleteExpense(Long id, User user) {
    Optional<Expense> existingExpense = expenseRepository.findByIdAndUser(id, user);
    if (existingExpense.isPresent()) {
      expenseRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
