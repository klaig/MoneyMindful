package io.github.kevinlaig.backend.mapper;

import io.github.kevinlaig.backend.dto.CreateCategoryDto;
import io.github.kevinlaig.backend.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for category-related operations.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)  // User is set in the service layer
  Category toEntity(CreateCategoryDto dto);
}
