package io.github.kevinlaig.backend.service;

import io.github.kevinlaig.backend.dto.SignupDto;
import io.github.kevinlaig.backend.dto.UpdateUserDto;
import io.github.kevinlaig.backend.mapper.UserMapper;
import io.github.kevinlaig.backend.model.Role;
import io.github.kevinlaig.backend.model.Roles;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for user operations.
 */
@Service
public class UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final UserMapper userMapper;

  @Autowired
  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.userMapper = userMapper;
  }

  /**
   * Create a new user.
   *
   * @param signupDTO DTO containing user details
   * @return the created user
   */
  public User createUser(SignupDto signupDTO) {
    // Validates the new user data
    validateNewUserData(signupDTO);

    // Maps the DTO to an entity and encodes the password
    User user = userMapper.toEntity(signupDTO);
    user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));

    // Sets the user role to USER for every new user
    Role userRole = new Role(Roles.USER);
    user.setRole(userRole);

    return userRepository.save(user);
  }

  /**
   * Validate new user data.
   *
   * @param signupDTO DTO containing user details
   */
  private void validateNewUserData(SignupDto signupDTO) {
    if (signupDTO.getUsername() == null || signupDTO.getUsername().trim().isEmpty()) {
      throw new IllegalArgumentException("Username cannot be null or empty");
    }
    if (signupDTO.getPassword() == null || signupDTO.getPassword().trim().isEmpty()) {
      throw new IllegalArgumentException("Password cannot be null or empty");
    }
    userRepository.findByUsername(signupDTO.getUsername()).ifPresent(u -> {
      throw new IllegalStateException("Username already taken");
    });
  }

  /**
   * Get a user by ID.
   *
   * @param id User ID
   * @return the user
   */
  @PreAuthorize("hasRole('ADMIN')")
  public Optional<User> getUserById(Long id) {
    return userRepository.findById(id);
  }

  /**
   * Get a user by username.
   *
   * @param username Username
   * @return the user
   */
  @PreAuthorize("#username == authentication.principal.username")
  public Optional<User> getUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  /**
   * Get all users.
   *
   * @return list of users
   */
  @PreAuthorize("hasRole('ADMIN')")
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  /**
   * Update a user.
   *
   * @param updateUserDTO DTO containing user details
   * @return the updated user
   */
  @Transactional
  @PreAuthorize("#updateUserDTO.username == authentication.principal.username")
  public User updateUser(UpdateUserDto updateUserDTO) {
    return userRepository.findByUsername(updateUserDTO.getUsername())
      .map(user -> {
        user.setEmail(updateUserDTO.getEmail());
        user.setFullName(updateUserDTO.getFullName());
        user.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
        return userRepository.save(user);
      })
      .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + updateUserDTO.getUsername()));
  }

  /**
   * Delete a user by ID.
   *
   * @param id User ID
   */
  @PreAuthorize("hasRole('ADMIN')")
  public boolean deleteUser(Long id) {
    return userRepository.findById(id)
      .map(user -> {
        userRepository.deleteById(id);
        return true;
      })
      .orElse(false);
  }
}
