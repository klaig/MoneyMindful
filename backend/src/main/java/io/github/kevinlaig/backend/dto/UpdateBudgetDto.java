package io.github.kevinlaig.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO for updating a budget.
 */
@Getter
@Setter
public class UpdateBudgetDto {
  @NotNull(message = "Budget limits cannot be null")
  private Map<@NotNull(message = "Category ID cannot be null") Long,
              @Positive(message = "Budget limit must be positive") BigDecimal> budgetLimits;
}
