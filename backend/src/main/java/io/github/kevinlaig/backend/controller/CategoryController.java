package io.github.kevinlaig.backend.controller;

import io.github.kevinlaig.backend.dto.CreateCategoryDto;
import io.github.kevinlaig.backend.dto.UpdateCategoryDto;
import io.github.kevinlaig.backend.model.Category;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.UserRepository;
import io.github.kevinlaig.backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Controller for managing categories.
 */
@RestController
@RequestMapping("/api/user/categories")
public class CategoryController {

  private final CategoryService categoryService;
  private final UserRepository userRepository;

  @Autowired
  public CategoryController(CategoryService categoryService, UserRepository userRepository) {
    this.categoryService = categoryService;
    this.userRepository = userRepository;
  }

  /**
   * Create a new category.
   *
   * @param createCategoryDto DTO containing category data
   * @param principal         authenticated user
   * @return created category
   */
  @PostMapping
  public ResponseEntity<Category> createCategory(@RequestBody @Valid CreateCategoryDto createCategoryDto, Principal principal) {
    User user = userRepository.findByUsername(principal.getName())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    Category createdCategory = categoryService.createCategory(createCategoryDto, user);
    return ResponseEntity.ok(createdCategory);
  }

  /**
   * Get all categories for the authenticated user.
   *
   * @param principal authenticated user
   * @return list of categories
   */
  @GetMapping
  public ResponseEntity<List<Category>> getAllUserCategories(Principal principal) {
    User user = userRepository.findByUsername(principal.getName())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    List<Category> categories = categoryService.getAllUserCategories(user);
    return ResponseEntity.ok(categories);
  }

  /**
   * Get a category by ID.
   *
   * @param id        category ID
   * @param principal authenticated user
   * @return category
   */
  @GetMapping("/{id}")
  public ResponseEntity<Category> getCategoryById(@PathVariable Long id, Principal principal) {
    User user = userRepository.findByUsername(principal.getName())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    Optional<Category> category = categoryService.findCategoryByIdAndUser(id, user);
    return category.map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Update a category.
   *
   * @param id        category ID
   * @param updateDto DTO containing updated category data
   * @param principal authenticated user
   * @return updated category
   */
  @PutMapping("/{id}")
  public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody @Valid UpdateCategoryDto updateDto, Principal principal) {
    User user = userRepository.findByUsername(principal.getName())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    Optional<Category> updatedCategory = categoryService.updateCategory(id, updateDto, user);
    return updatedCategory.map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Delete a category.
   *
   * @param id        category ID
   * @param principal authenticated user
   * @return 200 OK if successful, 404 Not Found if category not found
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteCategory(@PathVariable Long id, Principal principal) {
    User user = userRepository.findByUsername(principal.getName())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    boolean isDeleted = categoryService.deleteCategory(id, user);
    if (isDeleted) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
