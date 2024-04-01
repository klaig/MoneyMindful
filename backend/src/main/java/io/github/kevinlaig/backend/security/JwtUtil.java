package io.github.kevinlaig.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Utility class for JWT operations.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration.time}")
    private long expirationTime;

    /**
     * Generates a JWT token.
     *
     * @param username the username
     * @return the JWT token
     */
    public String generateToken(String username) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + expirationTime);

        return JWT.create()
                .withSubject(username)
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .sign(Algorithm.HMAC256(secret));
    }

    /**
     * Validates a JWT token.
     *
     * @param token the token
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            // Log the exception details
            return false;
        }
    }

    /**
     * Extracts the username from a JWT token.
     *
     * @param token the token
     * @return the username
     */
    public String getUsernameFromToken(String token) {
        DecodedJWT decodedJwt = JWT.decode(token);
        return decodedJwt.getSubject();
    }
}
