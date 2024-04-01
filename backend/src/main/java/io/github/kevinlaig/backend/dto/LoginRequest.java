package io.github.kevinlaig.backend.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Request for login endpoint.
 */
@Getter
@Setter
public class LoginRequest {
    private String username;
    private String password;

}
