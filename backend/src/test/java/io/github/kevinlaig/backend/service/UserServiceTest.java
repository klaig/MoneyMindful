package io.github.kevinlaig.backend.service;

import io.github.kevinlaig.backend.dto.SignupDTO;
import io.github.kevinlaig.backend.model.User;
import io.github.kevinlaig.backend.repository.UserRepository;
import io.github.kevinlaig.backend.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

  private SignupDTO signupDTO;
  private User user;

  @BeforeEach
  void setUp() {
    signupDTO = new SignupDTO();
    signupDTO.setUsername("testUser");
    signupDTO.setPassword("password123");

    user = new User();
    user.setUsername(signupDTO.getUsername());
    user.setPassword("encryptedPassword123");
  }

  @Test
  void createUser_WhenUserDoesNotExist_ShouldCreateUser() {
    when(userRepository.findByUsername(signupDTO.getUsername())).thenReturn(Optional.empty());
    when(passwordEncoder.encode(signupDTO.getPassword())).thenReturn(user.getPassword());
    when(userMapper.toEntity(signupDTO)).thenReturn(user);
    when(userRepository.save(any(User.class))).thenReturn(user);

    User createdUser = userService.createUser(signupDTO);

    assertNotNull(createdUser);
    assertEquals(signupDTO.getUsername(), createdUser.getUsername());
    assertEquals(user.getPassword(), createdUser.getPassword());
    verify(userRepository, times(1)).save(user);
  }

  @Test
  void createUser_WhenUserAlreadyExists_ShouldThrowException() {
    when(userRepository.findByUsername(signupDTO.getUsername())).thenReturn(Optional.of(user));

    assertThrows(IllegalStateException.class, () -> userService.createUser(signupDTO));
    verify(userRepository, never()).save(any(User.class));
  }

}
