package io.github.kevinlaig.backend.mapper;

import io.github.kevinlaig.backend.dto.CreateBudgetLimitDto;
import io.github.kevinlaig.backend.model.BudgetLimit;
import io.github.kevinlaig.backend.model.Category;
import io.github.kevinlaig.backend.repository.CategoryRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper for budget limit-related operations.
 */
@Mapper(componentModel = "spring")
public abstract class BudgetLimitMapper {

  @Autowired
  private CategoryRepository categoryRepository;

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "budget", ignore = true) // Budget is set in the service layer
  @Mapping(target = "category", expression = "java(mapCategory(dto.getCategoryId()))")
  public abstract BudgetLimit toEntity(CreateBudgetLimitDto dto);

  protected Category mapCategory(Long categoryId) {
    return categoryId == null ? null : categoryRepository.findById(categoryId).orElse(null);
  }
}
