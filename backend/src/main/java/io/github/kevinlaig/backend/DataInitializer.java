package io.github.kevinlaig.backend;

import io.github.kevinlaig.backend.model.*;
import io.github.kevinlaig.backend.repository.CategoryRepository;
import io.github.kevinlaig.backend.repository.ExpenseRepository;
import io.github.kevinlaig.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data initializer for the application.
 */
@Component
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ExpenseRepository expenseRepository;

    private final CategoryRepository categoryRepository;

    @Autowired
    public DataInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           ExpenseRepository expenseRepository,
                           CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
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
        User user = userRepository.findByUsername("kevin").orElse(null);
        if (user != null && expenseRepository.findByUser(user).isEmpty()) {
            // Create a category
            Category foodCategory = new Category();
            foodCategory.setName("Food");
            foodCategory.setUser(user);
            categoryRepository.save(foodCategory);

            // Create an expense with the category
            expenseRepository.save(new Expense(
              null, user, new BigDecimal("10.0"), foodCategory, LocalDateTime.now(), "Lunch"));
        }
        if (userRepository.findByUsername("lisa").isEmpty()) {
            // Create user
            Role userRole = new Role(Roles.USER);

            // Create normal user
            User normalUser = new User("lisa@example.com",
              "lisa",
              passwordEncoder.encode("lisa123"),
              "Lisa Brand",
              userRole);

            userRepository.save(normalUser);
        }
    }
}
