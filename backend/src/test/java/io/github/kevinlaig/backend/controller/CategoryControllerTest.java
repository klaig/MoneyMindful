package io.github.kevinlaig.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kevinlaig.backend.dto.CreateCategoryDto;
import io.github.kevinlaig.backend.dto.UpdateCategoryDto;
import io.github.kevinlaig.backend.model.Category;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.UserRepository;
import io.github.kevinlaig.backend.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

  @Mock
  private CategoryService categoryService;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private CategoryController categoryController;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper = new ObjectMapper();
  private User user;
  private Category category;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper().findAndRegisterModules();
    mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    user = new User(1L, "user@example.com", "testUser", "password", "Test User", null);
    category = new Category(user, "Travel");
    category.setId(1L);
  }

  @Test
  void createCategory_ValidData_ReturnsCreatedCategory() throws Exception {
    CreateCategoryDto createDto = new CreateCategoryDto();
    createDto.setName("Travel");
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
    when(categoryService.createCategory(any(CreateCategoryDto.class), eq(user))).thenReturn(category);

    mockMvc.perform(post("/api/user/categories")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createDto))
        .principal(() -> "testUser"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("Travel"));
  }

  @Test
  void getAllUserCategories_ReturnsCategoriesList() throws Exception {
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
    when(categoryService.getAllUserCategories(user)).thenReturn(Collections.singletonList(category));

    mockMvc.perform(get("/api/user/categories")
        .principal(() -> "testUser"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].name").value("Travel"));
  }

  @Test
  void getCategoryById_ExistingId_ReturnsCategory() throws Exception {
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
    when(categoryService.findCategoryByIdAndUser(1L, user)).thenReturn(Optional.of(category));

    mockMvc.perform(get("/api/user/categories/{id}", 1)
        .principal(() -> "testUser"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("Travel"));
  }

  @Test
  void updateCategory_ExistingCategory_UpdatesAndReturnsUpdatedCategory() throws Exception {
    Long categoryId = 1L;
    UpdateCategoryDto updateDto = new UpdateCategoryDto();
    updateDto.setName("Updated Travel");

    Category updatedCategory = new Category(user, "Updated Travel");
    updatedCategory.setId(categoryId);

    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
    when(categoryService.updateCategory(eq(categoryId), any(UpdateCategoryDto.class), eq(user)))
      .thenReturn(Optional.of(updatedCategory));

    mockMvc.perform(put("/api/user/categories/{id}", categoryId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateDto))
        .principal(() -> "testUser"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("Updated Travel"));
  }

  @Test
  void deleteCategory_ExistingId_DeletesCategory() throws Exception {
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
    when(categoryService.deleteCategory(1L, user)).thenReturn(true);

    mockMvc.perform(delete("/api/user/categories/{id}", 1)
        .principal(() -> "testUser"))
      .andExpect(status().isOk());
  }
}
