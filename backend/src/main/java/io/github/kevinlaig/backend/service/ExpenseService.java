package io.github.kevinlaig.backend.service;

import io.github.kevinlaig.backend.dto.CreateExpenseDto;
import io.github.kevinlaig.backend.dto.ExpenseDto;
import io.github.kevinlaig.backend.dto.UpdateExpenseDto;
import io.github.kevinlaig.backend.mapper.ExpenseMapper;
import io.github.kevinlaig.backend.model.Expense;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.CategoryRepository;
import io.github.kevinlaig.backend.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service for expenses.
 */
@Service
public class ExpenseService {

  private final ExpenseRepository expenseRepository;

  private final CategoryRepository categoryRepository;

  private final ExpenseMapper expenseMapper;

  @Autowired
  public ExpenseService(ExpenseRepository expenseRepository, CategoryRepository categoryRepository, ExpenseMapper expenseMapper) {
    this.expenseRepository = expenseRepository;
    this.categoryRepository = categoryRepository;
    this.expenseMapper = expenseMapper;
  }

  /**
   * Create an Expense.
   *
   * @param createExpenseDto CreateExpenseDto
   * @param user             User
   * @return Expense
   */
  @Transactional
  @PreAuthorize("#user.username == authentication.principal.username")
  public Expense createExpense(CreateExpenseDto createExpenseDto, User user) {
    Expense expense = expenseMapper.toEntity(createExpenseDto);

    expense.setUser(user);

    return expenseRepository.save(expense);
  }


  /**
   * Get all Expenses for a User.
   *
   * @param user User
   * @return List of Expenses
   */
  @PreAuthorize("#user.username == authentication.principal.username")
  public List<ExpenseDto> getAllUserExpenses(User user) {
    ArrayList<ExpenseDto> expenses = new ArrayList<>();
    expenseRepository.findByUser(user).forEach(expense -> expenses.add(expenseMapper.toDto(expense)));
    return expenses;
  }

  /**
   * Find an Expense by ID and User.
   *
   * @param id   Long
   * @param user User
   * @return Optional of Expense
   */
  @PreAuthorize("#user.username == authentication.principal.username")
  public Optional<ExpenseDto> findExpenseByIdAndUser(Long id, User user) {
    return expenseRepository.findByIdAndUser(id, user).map(expenseMapper::toDto);
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
