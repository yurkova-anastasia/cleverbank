package by.yurkova.cleverbank.mapper;

import by.yurkova.cleverbank.dto.UserRequestDto;
import by.yurkova.cleverbank.dto.UserResponseDto;
import by.yurkova.cleverbank.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper interface responsible for mapping between User entities and their corresponding DTOs.
 * It provides methods for converting User objects to UserResponseDto objects and vice versa.
 * Additionally, it supports mapping lists of User entities to lists of UserResponseDto objects.
 *
 * @author Yurkova Anastacia
 * @see User
 * @see UserResponseDto
 */
@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    UserResponseDto toDto(User user);

    User fromDto(UserRequestDto dto);

    List<UserResponseDto> toListOfDto(List<User> users);
}
