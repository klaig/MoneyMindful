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
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserDetailsServiceTest {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        // Clean up the database
        userRepository.findByEmail("MariMaasikas@gmail.com").ifPresent(user -> userRepository.delete(user));

        // Create a role
        Role role = new Role(Roles.USER);

        // Create a test user
        User testUser = new User("MariMaasikas@gmail.com", "Mari1998", passwordEncoder.encode("MariParool"), "Mari Maasikas", role); // Set user details
        testUser = userRepository.save(testUser);
    }

    @Test
    public void shouldLoadUserByUsername() {
        UserDetails userDetails = userDetailsService.loadUserByUsername("Mari1998");
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("Mari1998");
        assertThat(userDetails.getAuthorities()).isNotEmpty();
    }
}
