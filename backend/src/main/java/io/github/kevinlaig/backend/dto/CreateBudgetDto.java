package io.github.kevinlaig.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * DTO for creating a budget.
 */
@Getter
@Setter
public class CreateBudgetDto {
  @NotEmpty(message = "Budget limits cannot be empty")
  private Set<@Valid CreateBudgetLimitDto> budgetLimits;
}
