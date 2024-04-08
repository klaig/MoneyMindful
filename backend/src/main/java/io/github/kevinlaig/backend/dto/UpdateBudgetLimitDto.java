package io.github.kevinlaig.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateBudgetLimitDto {
  @NotNull(message = "Budget ID is required")
  private Long budgetId;

  @NotNull(message = "Category ID is required")
  private Long categoryId;

  @NotNull(message = "Limit amount is required")
  @DecimalMin(value = "0.0", inclusive = false, message = "Limit amount must be greater than 0")
  private BigDecimal limitAmount;
}
