package io.github.kevinlaig.backend.controller;

import io.github.kevinlaig.backend.dto.CreateSavingsGoalDto;
import io.github.kevinlaig.backend.dto.UpdateSavingsGoalDto;
import io.github.kevinlaig.backend.model.SavingsGoal;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.UserRepository;
import io.github.kevinlaig.backend.service.SavingsGoalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Controller for managing savings goals.
 */
@RestController
@RequestMapping("/api/user/savingsgoals")
public class SavingsGoalController {

  private final SavingsGoalService savingsGoalService;
  private final UserRepository userRepository;

  @Autowired
  public SavingsGoalController(SavingsGoalService savingsGoalService, UserRepository userRepository) {
    this.savingsGoalService = savingsGoalService;
    this.userRepository = userRepository;
  }

  /**
   * Create a new savings goal.
   *
   * @param createSavingsGoalDto CreateSavingsGoalDto
   * @param principal            Principal
   * @return Created savings goal
   */
  @PostMapping
  public ResponseEntity<SavingsGoal> createSavingsGoal(@RequestBody @Valid CreateSavingsGoalDto createSavingsGoalDto, Principal principal) {
    User user = userRepository.findByUsername(principal.getName())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    SavingsGoal createdSavingsGoal = savingsGoalService.createSavingsGoal(createSavingsGoalDto, user);
    return ResponseEntity.ok(createdSavingsGoal);
  }

  /**
   * Get all savings goals for the authenticated user.
   *
   * @param principal Principal
   * @return List of savings goals
   */
  @GetMapping
  public ResponseEntity<List<SavingsGoal>> getAllUserSavingsGoals(Principal principal) {
    User user = userRepository.findByUsername(principal.getName())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    List<SavingsGoal> savingsGoals = savingsGoalService.getAllUserSavingsGoals(user);
    return ResponseEntity.ok(savingsGoals);
  }

  /**
   * Get a savings goal by ID.
   *
   * @param id        Savings goal ID
   * @param principal Principal
   * @return Response entity
   */
  @GetMapping("/{id}")
  public ResponseEntity<SavingsGoal> getSavingsGoalById(@PathVariable Long id, Principal principal) {
    User user = userRepository.findByUsername(principal.getName())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    Optional<SavingsGoal> savingsGoal = savingsGoalService.findSavingsGoalById(id, user);
    return savingsGoal.map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Update a savings goal.
   *
   * @param id                   Savings goal ID
   * @param updateSavingsGoalDto UpdateSavingsGoalDto
   * @param principal            Principal
   * @return Updated savings goal
   */
  @PutMapping("/{id}")
  public ResponseEntity<SavingsGoal> updateSavingsGoal(@PathVariable Long id, @RequestBody @Valid UpdateSavingsGoalDto updateSavingsGoalDto, Principal principal) {
    User user = userRepository.findByUsername(principal.getName())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    Optional<SavingsGoal> updatedSavingsGoal = savingsGoalService.updateSavingsGoal(id, updateSavingsGoalDto, user);
    return updatedSavingsGoal.map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Delete a savings goal.
   *
   * @param id        Savings goal ID
   * @param principal Principal
   * @return Response entity
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteSavingsGoal(@PathVariable Long id, Principal principal) {
    User user = userRepository.findByUsername(principal.getName())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    boolean isDeleted = savingsGoalService.deleteSavingsGoal(id, user);
    if (isDeleted) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
