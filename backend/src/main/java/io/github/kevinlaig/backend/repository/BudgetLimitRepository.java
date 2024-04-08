package io.github.kevinlaig.backend.repository;

import io.github.kevinlaig.backend.model.BudgetLimit;
import io.github.kevinlaig.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BudgetLimitRepository extends JpaRepository<BudgetLimit, Long>{
  @Query("SELECT bl FROM BudgetLimit bl WHERE bl.id = :id AND bl.budget.user = :user")
  Optional<BudgetLimit> findByIdAndUser(@Param("id") Long id, @Param("user") User user);
}

