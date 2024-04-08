package io.github.kevinlaig.backend.service;

import io.github.kevinlaig.backend.dto.UpdateCategoryDto;
import io.github.kevinlaig.backend.model.Category;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for category operations.
 */
@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  @Autowired
  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  /**
   * Create a category.
   *
   * @param category Category
   * @param user     User
   * @return the created category
   */
  @Transactional
  @PreAuthorize("#user.username == authentication.principal.username")
  public Category createCategory(Category category, User user) {
    return categoryRepository.save(category);
  }

  /**
   * Find a category by ID and user.
   *
   * @param id   Category ID
   * @param user User
   * @return Optional of category
   */
  @PreAuthorize("#user.username == authentication.principal.username")
  public Optional<Category> findCategoryByIdAndUser(Long id, User user) {
    return categoryRepository.findByIdAndUser(id, user);
  }

  /**
   * Get all categories for a user.
   *
   * @param user User
   * @return List of categories
   */
  @PreAuthorize("#user.username == authentication.principal.username")
  public List<Category> getAllUserCategories(User user) {
    return categoryRepository.findByUser(user);
  }

  /**
   * Update a category.
   *
   * @param categoryId Category ID
   * @param updateDto UpdateCategoryDto
   * @param user      User
   * @return Optional of updated category
   */
  @Transactional
  @PreAuthorize("#user.username == authentication.principal.username")
  public Optional<Category> updateCategory(Long categoryId, UpdateCategoryDto updateDto, User user) {
    return categoryRepository.findByIdAndUser(categoryId, user).map(category -> {
      category.setName(updateDto.getName());
      return categoryRepository.save(category);
    });
  }

  /**
   * Delete a category.
   *
   * @param id   Category ID
   * @param user User
   * @return true if the category was deleted, false otherwise
   */
  @Transactional
  @PreAuthorize("#user.username == authentication.principal.username")
  public boolean deleteCategory(Long id, User user) {
    Optional<Category> existingCategory = categoryRepository.findByIdAndUser(id, user);
    if (existingCategory.isPresent()) {
      categoryRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
