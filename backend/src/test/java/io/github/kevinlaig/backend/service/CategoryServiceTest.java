package io.github.kevinlaig.backend.service;

import io.github.kevinlaig.backend.dto.UpdateCategoryDto;
import io.github.kevinlaig.backend.model.Category;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for category service.
 */
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private CategoryService categoryService;

  private User testUser;
  private Category testCategory;

  @BeforeEach
  void setUp() {
    testUser = new User("test@example.com", "testUser", "password", "Test User", null);
    testCategory = new Category(testUser, "TestCategory");
  }

  @Test
  void createCategory_SuccessfulCreation_ReturnsCategory() {
    when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

    Category result = categoryService.createCategory(testCategory, testUser);

    assertNotNull(result);
    assertEquals(testCategory, result);
  }

  @Test
  void findCategoryByIdAndUser_ValidIdAndUser_ReturnsCategory() {
    when(categoryRepository.findByIdAndUser(testCategory.getId(), testUser)).thenReturn(Optional.of(testCategory));

    Optional<Category> result = categoryService.findCategoryByIdAndUser(testCategory.getId(), testUser);

    assertTrue(result.isPresent());
    assertEquals(testCategory, result.get());
  }

  @Test
  void getAllUserCategories_ValidUser_ReturnsCategoriesList() {
    List<Category> categories = Arrays.asList(testCategory, new Category(testUser, "OtherCategory"));
    when(categoryRepository.findByUser(testUser)).thenReturn(categories);

    List<Category> result = categoryService.getAllUserCategories(testUser);

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(2, result.size());
  }

  @Test
  void updateCategory_ExistingCategory_UpdatesAndReturnsCategory() {
    UpdateCategoryDto updateDto = new UpdateCategoryDto();
    updateDto.setName("UpdatedCategory");
    when(categoryRepository.findByIdAndUser(testCategory.getId(), testUser)).thenReturn(Optional.of(testCategory));
    when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

    Optional<Category> result = categoryService.updateCategory(testCategory.getId(), updateDto, testUser);

    assertTrue(result.isPresent());
    assertEquals("UpdatedCategory", result.get().getName());
  }

  @Test
  void deleteCategory_ExistingCategory_DeletesAndReturnsTrue() {
    when(categoryRepository.findByIdAndUser(testCategory.getId(), testUser)).thenReturn(Optional.of(testCategory));
    doNothing().when(categoryRepository).deleteById(testCategory.getId());

    boolean result = categoryService.deleteCategory(testCategory.getId(), testUser);

    assertTrue(result);
  }

  @Test
  void deleteCategory_NonExistingCategory_ReturnsFalse() {
    when(categoryRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.empty());

    boolean result = categoryService.deleteCategory(1L, testUser);

    assertFalse(result);
  }
}
