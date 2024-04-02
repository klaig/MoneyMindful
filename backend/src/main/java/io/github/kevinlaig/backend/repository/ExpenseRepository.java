package io.github.kevinlaig.backend.repository;

import io.github.kevinlaig.backend.model.Expense;
import io.github.kevinlaig.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for expenses.
 */
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
  List<Expense> findByUser(User user);
}
