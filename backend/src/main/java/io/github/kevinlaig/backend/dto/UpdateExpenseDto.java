package io.github.kevinlaig.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for updating an expense.
 */
@Getter
@Setter
public class UpdateExpenseDto {
  @NotNull(message = "Amount is required")
  @Min(value = 0, message = "Amount must be greater than 0")
  private BigDecimal amount;

  @NotNull(message = "Category ID is required")
  private Long categoryId;

  @NotNull(message = "Date and time are required")
  private LocalDateTime dateTime;

  @NotNull(message = "Notes are required")
  @Size(max = 500, message = "Notes cannot be longer than 500 characters")
  private String notes;


}
