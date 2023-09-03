package by.yurkova.cleverbank.service;

import by.yurkova.cleverbank.dto.UserRequestDto;
import by.yurkova.cleverbank.dto.UserResponseDto;

import java.util.List;

/**
 * Service interface for managing users.
 *
 * @author Yurkova Anastacia
 */
public interface UserService {

    UserResponseDto findById(Long id);

    UserResponseDto findByAccountId(Long accountId);

    List<UserResponseDto> findAll(int limit, int offset);

    UserResponseDto save(UserRequestDto user);

    boolean updateById(Long id, UserRequestDto userDto);

    boolean deleteById(Long id);
}
