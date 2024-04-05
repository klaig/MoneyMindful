package io.github.kevinlaig.backend.mapper;

import org.mapstruct.Mapper;
import io.github.kevinlaig.backend.dto.SignupDTO;
import io.github.kevinlaig.backend.model.User;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
  @Mapping(target = "id", ignore = true)      // Ignore id when mapping
  @Mapping(target = "role", ignore = true)    // Ignore role when mapping
  User toEntity(SignupDTO dto);

  SignupDTO toDTO(User user);
}
