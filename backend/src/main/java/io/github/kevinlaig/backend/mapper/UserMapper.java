package io.github.kevinlaig.backend.mapper;

import io.github.kevinlaig.backend.dto.SignupDto;
import org.mapstruct.Mapper;
import io.github.kevinlaig.backend.model.User;
import org.mapstruct.Mapping;

/**
 * Mapper for user-related operations.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
  @Mapping(target = "id", ignore = true)      // Ignore id when mapping
  @Mapping(target = "role", ignore = true)    // Ignore role when mapping
  User toEntity(SignupDto dto);

  SignupDto toDto(User user);
}
