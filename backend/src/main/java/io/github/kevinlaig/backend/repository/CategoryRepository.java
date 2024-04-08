package io.github.kevinlaig.backend.repository;

import io.github.kevinlaig.backend.model.Category;
import io.github.kevinlaig.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for categories.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findByIdAndUser(Long id, User user);
  List<Category> findByUser(User user);
}
