package io.github.kevinlaig.backend.mapper;

import io.github.kevinlaig.backend.dto.CreateSavingsGoalDto;
import io.github.kevinlaig.backend.model.SavingsGoal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for savings goal-related operations.
 */
@Mapper(componentModel = "spring")
public interface SavingsGoalMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)  // User is set in the service layer
  @Mapping(target = "currentAmount", expression = "java(java.math.BigDecimal.ZERO)")  // Default value
  SavingsGoal toEntity(CreateSavingsGoalDto dto);
}
