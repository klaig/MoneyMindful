package io.github.kevinlaig.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for updating a user.
 */
@Getter
@Setter
public class UpdateUserDto {
  @NotNull
  @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
  private String username;

  @NotNull
  @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
  private String password;

  @NotNull
  @Email(message = "Email should be valid")
  private String email;

  @NotNull
  @Size(max = 100, message = "Full name must not exceed 100 characters")
  private String fullName;
}
