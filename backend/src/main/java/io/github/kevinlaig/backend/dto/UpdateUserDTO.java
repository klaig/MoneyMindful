package io.github.kevinlaig.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {
  private String username;
  private String password;
  private String email;
  private String fullName;
}
