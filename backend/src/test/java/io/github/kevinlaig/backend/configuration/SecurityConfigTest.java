package io.github.kevinlaig.backend.configuration;

import io.github.kevinlaig.backend.model.Role;
import io.github.kevinlaig.backend.model.Roles;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.UserRepository;
import io.github.kevinlaig.backend.security.JwtUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String token;

    @BeforeEach
    public void setup() {
        // Clean up the database
        userRepository.findByEmail("MariMaasikas@gmail.com").ifPresent(user -> userRepository.delete(user));

        // Create a role
        Role role = new Role(Roles.USER);

        // Create a test user
        User testUser = new User("MariMaasikas@gmail.com", "Mari1998", passwordEncoder.encode("MariParool"), "Mari Maasikas", role); // Set user details
        testUser = userRepository.save(testUser);

        // Generate token for the test user
        token = jwtUtil.generateToken(testUser.getUsername());
    }

    @Test
    public void shouldGiveForbiddenIfNoToken() throws Exception {
        mockMvc.perform(get("/api/secure-endpoint"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldGiveOkIfValidToken() throws Exception {
        mockMvc.perform(get("/api/secure-endpoint")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}