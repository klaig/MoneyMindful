package io.github.kevinlaig.backend.service;

import io.github.kevinlaig.backend.dto.SignupDto;
import io.github.kevinlaig.backend.dto.UpdateUserDto;
import io.github.kevinlaig.backend.model.Roles;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.UserRepository;
import io.github.kevinlaig.backend.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for user service.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private UserService userService;

  private SignupDto signupDTO;
  private User user;

  @BeforeEach
  void setUp() {
    signupDTO = new SignupDto();
    signupDTO.setUsername("testUser");
    signupDTO.setPassword("password123");

    user = new User();
    user.setUsername(signupDTO.getUsername());
    user.setPassword("encryptedPassword123");
  }

  @Test
  void createUser_WhenUserDoesNotExist_ShouldCreateUserWithUserRole() {
    when(userRepository.findByUsername(signupDTO.getUsername())).thenReturn(Optional.empty());
    when(passwordEncoder.encode(signupDTO.getPassword())).thenReturn(user.getPassword());
    when(userMapper.toEntity(signupDTO)).thenReturn(user);
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

    User createdUser = userService.createUser(signupDTO);

    assertNotNull(createdUser);
    assertEquals(signupDTO.getUsername(), createdUser.getUsername());
    assertEquals(user.getPassword(), createdUser.getPassword());
    assertNotNull(createdUser.getRole());
    assertEquals(Roles.USER, createdUser.getRole().getName());
    verify(userRepository).save(any(User.class));
  }

  @Test
  void createUser_WhenUserAlreadyExists_ShouldThrowException() {
    when(userRepository.findByUsername(signupDTO.getUsername())).thenReturn(Optional.of(user));

    assertThrows(IllegalStateException.class, () -> userService.createUser(signupDTO));
    verify(userRepository, never()).save(any(User.class));
  }



  @Test
  void findUserById_WhenUserExists_ShouldReturnUser() {
    Long userId = 1L;
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    Optional<User> foundUser = userService.findUserById(userId);

    assertTrue(foundUser.isPresent());
    assertEquals(user, foundUser.get());
    verify(userRepository).findById(userId);
  }

  @Test
  void findUserById_WhenUserDoesNotExist_ShouldReturnEmptyOptional() {
    Long userId = 1L;
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    Optional<User> foundUser = userService.findUserById(userId);

    assertFalse(foundUser.isPresent());
    verify(userRepository).findById(userId);
  }

  @Test
  void findUserByUsername_WhenUserExists_ShouldReturnUser() {
    String username = "testUser";
    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

    Optional<User> foundUser = userService.findUserByUsername(username);

    assertTrue(foundUser.isPresent());
    assertEquals(user, foundUser.get());
    verify(userRepository).findByUsername(username);
  }

  @Test
  void findUserByUsername_WhenUserDoesNotExist_ShouldReturnEmptyOptional() {
    String username = "nonExistentUser";
    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    Optional<User> foundUser = userService.findUserByUsername(username);

    assertFalse(foundUser.isPresent());
    verify(userRepository).findByUsername(username);
  }

  @Test
  void getAllUsers_ShouldReturnListOfUsers() {
    List<User> userList = Arrays.asList(user, new User());
    when(userRepository.findAll()).thenReturn(userList);

    List<User> result = userService.getAllUsers();

    assertEquals(userList.size(), result.size());
    verify(userRepository).findAll();
  }

  @Test
  void updateUser_WhenUserExists_ShouldUpdateAndReturnUser() {
    UpdateUserDto updateUserDTO = new UpdateUserDto();
    updateUserDTO.setUsername("testUser");
    updateUserDTO.setEmail("updated@example.com");
    updateUserDTO.setPassword("newPassword");
    updateUserDTO.setFullName("Updated Name");

    when(userRepository.findByUsername(updateUserDTO.getUsername())).thenReturn(Optional.of(user));
    when(passwordEncoder.encode(updateUserDTO.getPassword())).thenReturn("encryptedNewPassword");
    when(userRepository.save(any(User.class))).thenReturn(user);

    User updatedUser = userService.updateUser(updateUserDTO);

    assertEquals(updateUserDTO.getEmail(), updatedUser.getEmail());
    assertEquals(updateUserDTO.getFullName(), updatedUser.getFullName());
    assertEquals("encryptedNewPassword", updatedUser.getPassword());
    verify(userRepository).save(user);
  }

  @Test
  void updateUser_WhenUserDoesNotExist_ShouldThrowException() {
    UpdateUserDto updateUserDTO = new UpdateUserDto();
    updateUserDTO.setUsername("nonExistentUser");

    when(userRepository.findByUsername(updateUserDTO.getUsername())).thenReturn(Optional.empty());

    assertThrows(UsernameNotFoundException.class, () -> userService.updateUser(updateUserDTO));
  }


  @Test
  void deleteUser_WhenUserExists_ShouldReturnTrue() {
    Long userId = 1L;
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    doNothing().when(userRepository).deleteById(userId);

    boolean result = userService.deleteUser(userId);

    assertTrue(result);
    verify(userRepository).deleteById(userId);
  }

  @Test
  void deleteUser_WhenUserDoesNotExist_ShouldReturnFalse() {
    Long userId = 1L;
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    boolean result = userService.deleteUser(userId);

    assertFalse(result);
    verify(userRepository, never()).deleteById(anyLong());
  }

}
