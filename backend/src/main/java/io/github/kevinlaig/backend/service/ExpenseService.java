package io.github.kevinlaig.backend.service;

import io.github.kevinlaig.backend.dto.UpdateExpenseDto;
import io.github.kevinlaig.backend.model.Expense;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.CategoryRepository;
import io.github.kevinlaig.backend.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for expenses.
 */
@Service
public class ExpenseService {

  private final ExpenseRepository expenseRepository;

  private final CategoryRepository categoryRepository;

  @Autowired
  public ExpenseService(ExpenseRepository expenseRepository, CategoryRepository categoryRepository) {
    this.expenseRepository = expenseRepository;
    this.categoryRepository = categoryRepository;
  }

  /**
   * Create an Expense.
   *
   * @param expense Expense
   * @return Created Expense
   */
  @Transactional
  @PreAuthorize("#expense.user.username == authentication.principal.username")
  public Expense createExpense(Expense expense) {
    return expenseRepository.save(expense);
  }

  /**
   * Get all Expenses for a User.
   *
   * @param user User
   * @return List of Expenses
   */
  @PreAuthorize("#user.username == authentication.principal.username")
  public List<Expense> getAllUserExpenses(User user) {
    return expenseRepository.findByUser(user);
  }

  /**
   * Find an Expense by ID and User.
   *
   * @param id   Long
   * @param user User
   * @return Optional of Expense
   */
  @PreAuthorize("#user.username == authentication.principal.username")
  public Optional<Expense> findExpenseByIdAndUser(Long id, User user) {
    return expenseRepository.findByIdAndUser(id, user);
  }

  /**
   * Update an Expense.
   *
   * @param id            Long
   * @param expenseDetails UpdateExpenseDto
   * @param user          User
   * @return Optional of Expense
   */
  @Transactional
  @PreAuthorize("#user.username == authentication.principal.username")
  public Optional<Expense> updateExpense(Long id, UpdateExpenseDto expenseDetails, User user) {
    return expenseRepository.findByIdAndUser(id, user).map(expense -> {
      categoryRepository.findById(expenseDetails.getCategoryId()).ifPresent(expense::setCategory);
      expense.setAmount(expenseDetails.getAmount());
      expense.setDateTime(expenseDetails.getDateTime());
      expense.setNotes(expenseDetails.getNotes());
      return expenseRepository.save(expense);
    });
  }

  /**
   * Delete an Expense.
   *
   * @param id   Long
   * @param user User
   * @return boolean
   */
  @Transactional
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
