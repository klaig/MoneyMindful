package io.github.kevinlaig.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for creating a category.
 */
@Getter
@Setter
public class CreateCategoryDto {

  @NotBlank(message = "Catergory name is required")
  private String name;
}
