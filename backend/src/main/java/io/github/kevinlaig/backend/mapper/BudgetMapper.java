package io.github.kevinlaig.backend.mapper;

import io.github.kevinlaig.backend.dto.CreateBudgetDto;
import io.github.kevinlaig.backend.model.Budget;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for budget-related operations.
 */
@Mapper(componentModel = "spring", uses = { BudgetLimitMapper.class })
public interface BudgetMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true) // User is set in the service layer
  Budget toEntity(CreateBudgetDto dto);
}
