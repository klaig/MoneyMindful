package io.github.kevinlaig.backend.repository;

import io.github.kevinlaig.backend.model.SavingsGoal;
import io.github.kevinlaig.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Long> {
  Optional<SavingsGoal> findByIdAndUser(Long id, User user);
  List<SavingsGoal> findByUser(User user);
}
