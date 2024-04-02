package io.github.kevinlaig.backend.service;

import io.github.kevinlaig.backend.model.Role;
import io.github.kevinlaig.backend.model.Roles;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for user details service.
 */
@SpringBootTest
@ActiveProfiles("test")
public class UserDetailsServiceTest {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Setup before each test.
     */
    @BeforeEach
    public void setup() {
        // Creating a test user role
        Role role = new Role(Roles.USER);

        // Creating and saving the test user
        User testUser = new User("test@example.com", "kevin", "password", "Test User", role);
        userRepository.save(testUser);
    }

    @Test
    public void shouldLoadUserByUsername() {
        UserDetails userDetails = userDetailsService.loadUserByUsername("kevin");
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("kevin");
        assertThat(userDetails.getAuthorities()).isNotEmpty();
        assertThat(userDetails.getAuthorities()).extracting("authority")
                .contains("ROLE_USER");
    }
}
