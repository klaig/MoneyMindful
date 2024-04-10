package io.github.kevinlaig.backend.mapper;

import io.github.kevinlaig.backend.dto.CreateExpenseDto;
import io.github.kevinlaig.backend.model.Category;
import io.github.kevinlaig.backend.model.Expense;
import io.github.kevinlaig.backend.repository.CategoryRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ExpenseMapper {

  @Autowired
  private CategoryRepository categoryRepository;

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "category", expression = "java(mapCategory(createExpenseDto.getCategoryId()))")
  public abstract Expense toEntity(CreateExpenseDto createExpenseDto);

  protected Category mapCategory(Long categoryId) {
    return categoryId == null ? null : categoryRepository.findById(categoryId).orElse(null);
  }

}
