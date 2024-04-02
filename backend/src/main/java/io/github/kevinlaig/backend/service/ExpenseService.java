package io.github.kevinlaig.backend.service;

import io.github.kevinlaig.backend.model.Expense;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
  public Expense createExpense(Expense expense) {
    return expenseRepository.save(expense);
  }

  // Get all Expenses
  public List<Expense> findAllUserExpenses(User user) {
    return expenseRepository.findByUser(user);
  }

  // Get an Expense by ID
  public Optional<Expense> findExpenseById(Long id) {
    return expenseRepository.findById(id);
  }

  // Update an Expense
  public Expense updateExpense(Expense expense) {
    return expenseRepository.save(expense);
  }

  // Delete an Expense
  public void deleteExpense(Long id) {
    expenseRepository.deleteById(id);
  }
}
