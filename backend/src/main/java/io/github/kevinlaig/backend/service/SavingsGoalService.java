package io.github.kevinlaig.backend.service;

import io.github.kevinlaig.backend.dto.CreateSavingsGoalDto;
import io.github.kevinlaig.backend.dto.UpdateSavingsGoalDto;
import io.github.kevinlaig.backend.mapper.SavingsGoalMapper;
import io.github.kevinlaig.backend.model.SavingsGoal;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.SavingsGoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for savings goals.
 */
@Service
public class SavingsGoalService {

  private final SavingsGoalRepository savingsGoalRepository;

  private final SavingsGoalMapper savingsGoalMapper;

  @Autowired
  public SavingsGoalService(SavingsGoalRepository savingsGoalRepository, SavingsGoalMapper savingsGoalMapper) {
    this.savingsGoalRepository = savingsGoalRepository;
    this.savingsGoalMapper = savingsGoalMapper;
  }

  /**
   * Create a savings goal.
   *
   * @param createSavingsGoalDto CreateSavingsGoalDto
   * @param user                User
   * @return the created savings goal
   */
  @Transactional
  @PreAuthorize("#user.username == authentication.principal.username")
  public SavingsGoal createSavingsGoal(CreateSavingsGoalDto createSavingsGoalDto, User user) {
    SavingsGoal savingsGoal = savingsGoalMapper.toEntity(createSavingsGoalDto);
    savingsGoal.setUser(user);
    return savingsGoalRepository.save(savingsGoal);
  }

  /**
   * Get all SavingsGoals for a User.
   *
   * @param user User
   * @return List of SavingsGoals
   */
  @PreAuthorize("#user.username == authentication.principal.username")
  public List<SavingsGoal> getAllUserSavingsGoals(User user) {
    return savingsGoalRepository.findByUser(user);
  }

  /**
   * Get a savings goal by ID.
   *
   * @param id   Long
   * @param user User
   * @return Optional of SavingsGoal
   */
  @PreAuthorize("#user.username == authentication.principal.username")
  public Optional<SavingsGoal> findSavingsGoalById(Long id, User user) {
    return savingsGoalRepository.findByIdAndUser(id, user);
  }

  /**
   * Update a savings goal.
   *
   * @param id        Long
   * @param updateDTO UpdateSavingsGoalDto
   * @param user      User
   * @return Optional of SavingsGoal
   */
  @Transactional
  @PreAuthorize("#user.username == authentication.principal.username")
  public Optional<SavingsGoal> updateSavingsGoal(Long id, UpdateSavingsGoalDto updateDTO, User user) {
    return savingsGoalRepository.findByIdAndUser(id, user).map(savingsGoal -> {
      savingsGoal.setName(updateDTO.getName());
      savingsGoal.setTargetAmount(updateDTO.getTargetAmount());
      savingsGoal.setCurrentAmount(updateDTO.getCurrentAmount());
      savingsGoal.setTargetDate(updateDTO.getTargetDate());
      return savingsGoalRepository.save(savingsGoal);
    });
  }

  /**
   * Delete a savings goal.
   *
   * @param id   Long
   * @param user User
   * @return boolean
   */
  @Transactional
  @PreAuthorize("#user.username == authentication.principal.username")
  public boolean deleteSavingsGoal(Long id, User user) {
    Optional<SavingsGoal> existingSavingsGoal = savingsGoalRepository.findByIdAndUser(id, user);
    if (existingSavingsGoal.isPresent()) {
      savingsGoalRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
