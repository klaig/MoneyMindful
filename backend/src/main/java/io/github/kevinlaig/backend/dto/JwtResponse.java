package io.github.kevinlaig.backend.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Response containing a JWT token.
 */
@Getter
@Setter
public class JwtResponse {
    private String token;

    public JwtResponse(String token) {
        this.token = token;
    }

}