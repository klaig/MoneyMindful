package io.github.kevinlaig.backend.service;

import io.github.kevinlaig.backend.dto.UpdateSavingsGoalDto;
import io.github.kevinlaig.backend.model.SavingsGoal;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.SavingsGoalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

  @InjectMocks
  private SavingsGoalService savingsGoalService;

  private SavingsGoal savingsGoal;
  private User user;

  @BeforeEach
  void setUp() {
    user = new User();
    savingsGoal = new SavingsGoal();
    user.setUsername("testUser");
    savingsGoal.setUser(user);
  }

  @Test
  void whenCreateSavingsGoal_thenSaveSavingsGoal() {
    when(savingsGoalRepository.save(any(SavingsGoal.class))).thenReturn(savingsGoal);

    SavingsGoal created = savingsGoalService.createSavingsGoal(savingsGoal, user);

    assertNotNull(created);
    assertEquals(user, created.getUser());
    verify(savingsGoalRepository).save(savingsGoal);
  }

  @Test
  void whenGetAllUserSavingsGoals_thenReturnList() {
    List<SavingsGoal> goals = Collections.singletonList(savingsGoal);
    when(savingsGoalRepository.findByUser(user)).thenReturn(goals);

    List<SavingsGoal> result = savingsGoalService.getAllUserSavingsGoals(user);

    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
    verify(savingsGoalRepository).findByUser(user);
  }

  @Test
  void whenFindSavingsGoal_thenReturnSavingsGoal() {
    when(savingsGoalRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(savingsGoal));

    Optional<SavingsGoal> found = savingsGoalService.findSavingsGoal(1L, user);

    assertTrue(found.isPresent());
    assertEquals(savingsGoal, found.get());
    verify(savingsGoalRepository).findByIdAndUser(1L, user);
  }

  @Test
  void whenUpdateSavingsGoal_thenReturnUpdatedSavingsGoal() {
    UpdateSavingsGoalDto updateDTO = new UpdateSavingsGoalDto(); // Mock DTO
    when(savingsGoalRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(savingsGoal));
    when(savingsGoalRepository.save(any(SavingsGoal.class))).thenReturn(savingsGoal);

    Optional<SavingsGoal> updated = savingsGoalService.updateSavingsGoal(1L, updateDTO, user);

    assertTrue(updated.isPresent());
    verify(savingsGoalRepository).save(savingsGoal);
  }

  @Test
  void whenDeleteSavingsGoal_thenReturnTrue() {
    when(savingsGoalRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(savingsGoal));
    doNothing().when(savingsGoalRepository).deleteById(1L);

    assertTrue(savingsGoalService.deleteSavingsGoal(1L, user));
    verify(savingsGoalRepository).deleteById(1L);
  }

  @Test
  void whenDeleteNonExistingSavingsGoal_thenReturnFalse() {
    when(savingsGoalRepository.findByIdAndUser(1L, user)).thenReturn(Optional.empty());

    assertFalse(savingsGoalService.deleteSavingsGoal(1L, user));
    verify(savingsGoalRepository, never()).deleteById(1L);
  }
}
