package io.github.kevinlaig.backend.repository;

import io.github.kevinlaig.backend.model.Budget;
import io.github.kevinlaig.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
  Optional<Budget> findByIdAndUser(Long id, User user);

  List<Budget> findByUser(User user);
}

