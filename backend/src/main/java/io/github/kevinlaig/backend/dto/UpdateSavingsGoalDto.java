package io.github.kevinlaig.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for updating a savings goal.
 */
@Getter
@Setter
public class UpdateSavingsGoalDto {
  @NotNull
  @Length(max = 100, message = "Name must be less than 100 characters")
  private String name;

  @NotNull
  @DecimalMin(value = "0.0", inclusive = false, message = "Target amount must be greater than 0")
  private BigDecimal targetAmount;

  @NotNull
  @DecimalMin(value = "0.0", message = "Current amount must not be negative")
  private BigDecimal currentAmount;

  @NotNull
  @Future(message = "Target date must be in the future")
  private LocalDate targetDate;
}
