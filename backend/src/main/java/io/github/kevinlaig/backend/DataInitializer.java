package io.github.kevinlaig.backend;

import io.github.kevinlaig.backend.model.Role;
import io.github.kevinlaig.backend.model.Roles;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Data initializer for the application.
 */
@Component
@Profile("!test") // Exclude this runner in test profile
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin").isEmpty()) {
            // Create admin
            Role adminRole = new Role(Roles.ADMIN);

            // Create admin user
            User adminUser = new User("admin@example.com",
                    "admin",
                    passwordEncoder.encode("admin123"),
                    "Admin User",
                    adminRole);

            userRepository.save(adminUser);
        }
        if (userRepository.findByUsername("kevin").isEmpty()) {
            // Create user
            Role userRole = new Role(Roles.USER);

            // Create normal user
            User normalUser = new User("kevin@example.com",
                    "kevin",
                    passwordEncoder.encode("kevin123"),
                    "Kevin Laig",
                    userRole);

            userRepository.save(normalUser);
        }
    }
}
