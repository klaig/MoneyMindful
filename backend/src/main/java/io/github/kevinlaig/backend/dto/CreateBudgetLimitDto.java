package io.github.kevinlaig.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO for creating a budget limit.
 */
@Getter
@Setter
public class CreateBudgetLimitDto {
  @NotNull(message = "Category ID is required")
  private Long categoryId;

  @NotNull(message = "Limit amount is required")
  @Min(value = 0, message = "Limit amount must be greater than or equal to 0")
  private BigDecimal limitAmount;
}
