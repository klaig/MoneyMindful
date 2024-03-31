package io.github.kevinlaig.backend.controller;

import io.github.kevinlaig.backend.dto.JwtResponse;
import io.github.kevinlaig.backend.dto.LoginRequest;
import io.github.kevinlaig.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {


    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/api/auth/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext(); // Clear the security context
        return ResponseEntity.ok("Logout successful!");
    }
}
