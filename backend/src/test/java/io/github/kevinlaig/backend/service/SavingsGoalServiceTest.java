package io.github.kevinlaig.backend.service;

import io.github.kevinlaig.backend.dto.CreateSavingsGoalDto;
import io.github.kevinlaig.backend.dto.UpdateSavingsGoalDto;
import io.github.kevinlaig.backend.mapper.SavingsGoalMapper;
import io.github.kevinlaig.backend.model.SavingsGoal;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.SavingsGoalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for savings goal service.
 */
@ExtendWith(MockitoExtension.class)
class SavingsGoalServiceTest {

  @Mock
  private SavingsGoalRepository savingsGoalRepository;

  @Mock
  private SavingsGoalMapper savingsGoalMapper;

  @InjectMocks
  private SavingsGoalService savingsGoalService;

  private CreateSavingsGoalDto createSavingsGoalDto;
  private SavingsGoal savingsGoal;
  private User user;

  @BeforeEach
  void setUp() {
    user = new User();
    savingsGoal = new SavingsGoal();
    user.setUsername("testUser");
    savingsGoal.setUser(user);

    createSavingsGoalDto = new CreateSavingsGoalDto();
    createSavingsGoalDto.setName("Test Savings Goal");
  }

  @Test
  void createSavingsGoal_ValidSavingsGoal_ReturnsSavingsGoal() {
    when(savingsGoalRepository.save(any(SavingsGoal.class))).thenReturn(savingsGoal);
    when(savingsGoalMapper.toEntity(createSavingsGoalDto)).thenReturn(savingsGoal);

    SavingsGoal created = savingsGoalService.createSavingsGoal(createSavingsGoalDto, user);

    assertNotNull(created);
    assertEquals(user, created.getUser());
    verify(savingsGoalRepository).save(savingsGoal);
  }

  @Test
  void getAllUserSavingsGoals_ValidUser_ReturnsList() {
    List<SavingsGoal> goals = Collections.singletonList(savingsGoal);
    when(savingsGoalRepository.findByUser(user)).thenReturn(goals);

    List<SavingsGoal> result = savingsGoalService.getAllUserSavingsGoals(user);

    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
    verify(savingsGoalRepository).findByUser(user);
  }

  @Test
  void findSavingsGoal_ValidUser_ReturnsSavingsGoal() {
    when(savingsGoalRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(savingsGoal));

    Optional<SavingsGoal> found = savingsGoalService.findSavingsGoal(1L, user);

    assertTrue(found.isPresent());
    assertEquals(savingsGoal, found.get());
    verify(savingsGoalRepository).findByIdAndUser(1L, user);
  }

  @Test
  void updateSavingsGoal_ValidSavingsGoal_ReturnsUpdatedSavingsGoal() {
    UpdateSavingsGoalDto updateDTO = new UpdateSavingsGoalDto();
    when(savingsGoalRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(savingsGoal));
    when(savingsGoalRepository.save(any(SavingsGoal.class))).thenReturn(savingsGoal);

    Optional<SavingsGoal> updated = savingsGoalService.updateSavingsGoal(1L, updateDTO, user);

    assertTrue(updated.isPresent());
    verify(savingsGoalRepository).save(savingsGoal);
  }

  @Test
  void deleteSavingsGoal_ValidIdAndUser_DeletesAndReturnsTrue() {
    when(savingsGoalRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(savingsGoal));
    doNothing().when(savingsGoalRepository).deleteById(1L);

    assertTrue(savingsGoalService.deleteSavingsGoal(1L, user));
    verify(savingsGoalRepository).deleteById(1L);
  }

  @Test
  void deleteSavingsGoal_InvalidIdAndUser_ReturnsFalse() {
    when(savingsGoalRepository.findByIdAndUser(1L, user)).thenReturn(Optional.empty());

    assertFalse(savingsGoalService.deleteSavingsGoal(1L, user));
    verify(savingsGoalRepository, never()).deleteById(1L);
  }
}
