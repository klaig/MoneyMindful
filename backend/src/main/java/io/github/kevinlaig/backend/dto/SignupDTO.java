package io.github.kevinlaig.backend.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for signup endpoint.
 */
@Getter
@Setter
public class SignupDTO {
  private String username;
  private String password;
  private String email;
  private String fullName;
}
