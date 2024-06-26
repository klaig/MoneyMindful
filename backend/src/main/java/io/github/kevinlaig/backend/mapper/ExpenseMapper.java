package io.github.kevinlaig.backend.mapper;

import io.github.kevinlaig.backend.dto.CreateExpenseDto;
import io.github.kevinlaig.backend.dto.ExpenseDto;
import io.github.kevinlaig.backend.model.Category;
import io.github.kevinlaig.backend.model.Expense;
import io.github.kevinlaig.backend.repository.CategoryRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper for expense-related operations.
 */
@Mapper(componentModel = "spring")
public abstract class ExpenseMapper {

  @Autowired
  private CategoryRepository categoryRepository;

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)  // User is set in the service layer
  @Mapping(target = "category", expression = "java(mapCategory(createExpenseDto.getCategoryId()))")
  public abstract Expense toEntity(CreateExpenseDto createExpenseDto);

  protected Category mapCategory(Long categoryId) {
    return categoryId == null ? null : categoryRepository.findById(categoryId).orElse(null);
  }

  @Mapping(source = "category.name", target = "categoryName")
  public abstract ExpenseDto toDto(Expense expense);

}
