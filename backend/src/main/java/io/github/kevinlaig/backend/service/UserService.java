package io.github.kevinlaig.backend.service;

import io.github.kevinlaig.backend.dto.SignupDTO;
import io.github.kevinlaig.backend.mapper.UserMapper;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
  public User createUser(SignupDTO signupDTO) {
    validateNewUserData(signupDTO);
    User user = userMapper.toEntity(signupDTO);
    user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
    return userRepository.save(user);
  }

  /**
   * Validate new user data.
   *
   * @param signupDTO DTO containing user details
   */
  private void validateNewUserData(SignupDTO signupDTO) {
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

  public Optional<User> getUserById(Long id) {
    // Retrieve a user by ID
    return userRepository.findById(id);
  }

  public Optional<User> getUserByUsername(String username) {
    // Retrieve a user by username
    return userRepository.findByUsername(username);
  }

  public List<User> getAllUsers() {
    // Retrieve all users
    return userRepository.findAll();
  }

  public User updateUser(User user) {
    // Update a user's details
    // Ensure the user exists and handle changes appropriately
    return userRepository.save(user);
  }

  public void deleteUser(Long id) {
    // Delete a user by ID
    userRepository.deleteById(id);
  }
}
