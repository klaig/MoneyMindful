package io.github.kevinlaig.backend.controller;

import io.github.kevinlaig.backend.dto.SignupDto;
import io.github.kevinlaig.backend.dto.UpdateUserDto;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Controller for user-related operations.
 */
@RestController
@RequestMapping("/api")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Create a new user.
   *
   * @param signupDTO Signup DTO
   * @return Created user
   */
  @PostMapping("/user/signup")
  public ResponseEntity<User> createUser(@RequestBody @Valid SignupDto signupDTO) {
    User user = userService.createUser(signupDTO);
    return new ResponseEntity<>(user, HttpStatus.CREATED);
  }

  /**
   * Get a user by ID.
   *
   * @param id User ID
   * @return Response entity
   */
  @GetMapping("/admin/user/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Long id) {
    Optional<User> user = userService.getUserById(id);
    return user
      .map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Get a user by username.
   *
   * @param principal Principal
   * @return Response entity
   */
  @GetMapping("/user/profile")
  public ResponseEntity<?> getUserByUsername(Principal principal) {
    String username = principal.getName();
    Optional<User> user = userService.getUserByUsername(username);
    return user
      .map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Get all users.
   *
   * @return Response entity
   */
  @GetMapping("/admin/users")
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  /**
   * Update a user.
   *
   * @param updateUserDTO Update user DTO
   * @param principal Principal
   * @return Response entity
   */
  @PutMapping("/user/profile")
  public ResponseEntity<?> updateUser(@RequestBody @Valid UpdateUserDto updateUserDTO, Principal principal) {
    String username = principal.getName();
    if (!username.equals(updateUserDTO.getUsername())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
    }

    User updatedUser = userService.updateUser(updateUserDTO);
    return ResponseEntity.ok(updatedUser);
  }

  /**
   * Delete a user.
   *
   * @param id User ID
   * @return Response entity
   */
  @DeleteMapping("/admin/user/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    boolean isDeleted = userService.deleteUser(id);
    if (isDeleted) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
