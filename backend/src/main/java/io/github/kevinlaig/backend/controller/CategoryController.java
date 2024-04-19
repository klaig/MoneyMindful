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

  @PostMapping
  public ResponseEntity<Category> createCategory(@RequestBody @Valid CreateCategoryDto createCategoryDto, Principal principal) {
    User user = userRepository.findByUsername(principal.getName())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    Category createdCategory = categoryService.createCategory(createCategoryDto, user);
    return ResponseEntity.ok(createdCategory);
  }

  @GetMapping
  public ResponseEntity<List<Category>> getAllUserCategories(Principal principal) {
    User user = userRepository.findByUsername(principal.getName())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    List<Category> categories = categoryService.getAllUserCategories(user);
    return ResponseEntity.ok(categories);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Category> getCategoryById(@PathVariable Long id, Principal principal) {
    User user = userRepository.findByUsername(principal.getName())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    Optional<Category> category = categoryService.findCategoryByIdAndUser(id, user);
    return category.map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody @Valid UpdateCategoryDto updateDto, Principal principal) {
    User user = userRepository.findByUsername(principal.getName())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    Optional<Category> updatedCategory = categoryService.updateCategory(id, updateDto, user);
    return updatedCategory.map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

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
