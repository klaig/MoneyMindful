package io.github.kevinlaig.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ExpenseDto {

  private Long id; // For the frontend react list rendering
  private BigDecimal amount;
  private String categoryName;
  private LocalDateTime dateTime;
  private String notes;

}
