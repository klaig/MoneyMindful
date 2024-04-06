package io.github.kevinlaig.backend.repository;

import io.github.kevinlaig.backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for categories.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
