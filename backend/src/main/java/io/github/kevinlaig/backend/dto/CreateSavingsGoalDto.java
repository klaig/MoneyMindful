package io.github.kevinlaig.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for creating a savingsGoal.
 */
@Getter
@Setter
public class CreateSavingsGoalDto {

  @NotBlank(message = "Name is required")
  private String name;

  @NotNull(message = "Target amount is required")
  private BigDecimal targetAmount;

  @NotNull(message = "Target date is required")
  private LocalDate targetDate;

}
