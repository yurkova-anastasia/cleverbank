package by.yurkova.cleverbank.service.impl;

import by.yurkova.cleverbank.dto.UserRequestDto;
import by.yurkova.cleverbank.dto.UserResponseDto;
import by.yurkova.cleverbank.exception.EntityNotFoundException;
import by.yurkova.cleverbank.mapper.UserMapper;
import by.yurkova.cleverbank.model.User;
import by.yurkova.cleverbank.repository.UserRepository;
import by.yurkova.cleverbank.service.UserService;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Service implementation for managing users.
 *
 * @author Yurkova Anastacia
 */
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Finds a user by their unique identifier (ID).
     *
     * @param id The unique identifier of the user.
     * @return The UserResponseDto representing the found user.
     * @throws EntityNotFoundException If no user with the given ID is found.
     */
    @Override
    public UserResponseDto findById(Long id) {
        return userMapper.toDto(
                userRepository.findById(id).orElseThrow(() ->
                        new EntityNotFoundException("User with id = " + id + " was not found")));
    }

    /**
     * Finds a user by their associated account's unique identifier.
     *
     * @param accountId The unique identifier of the associated account.
     * @return The UserResponseDto representing the found user.
     * @throws EntityNotFoundException If no user with the given account ID is found.
     */
    @Override
    public UserResponseDto findByAccountId(Long accountId) {
        return userMapper.toDto(
                userRepository.findByAccountId(accountId).orElseThrow(() ->
                        new EntityNotFoundException("User with account id = " + accountId + " was not found")));
    }

    /**
     * Retrieves a list of users with pagination support.
     *
     * @param limit  The maximum number of users to retrieve.
     * @param offset The starting index for pagination.
     * @return A list of UserResponseDto representing users.
     */
    @Override
    public List<UserResponseDto> findAll(int limit, int offset) {
        return userMapper.toListOfDto(userRepository.findAll(limit, offset));
    }

    /**
     * Saves a new user based on the provided UserRequestDto.
     *
     * @param userDto The UserRequestDto containing user information.
     * @return The UserResponseDto representing the saved user.
     */
    @Override
    public UserResponseDto save(UserRequestDto userDto) {
        User user = userMapper.fromDto(userDto);
        return userMapper.toDto(userRepository.save(user));
    }

    /**
     * Updates an existing user based on the provided UserRequestDto.
     *
     * @param id      The unique identifier of the user to update.
     * @param userDto The UserRequestDto containing updated user information.
     * @return True if the user was successfully updated, false otherwise.
     * @throws EntityNotFoundException If no user with the given ID is found.
     */
    @Override
    public boolean updateById(Long id, UserRequestDto userDto) {
        userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User with id = " + id + " was not found"));
        User user = userMapper.fromDto(userDto);
        user.setId(id);
        return userRepository.update(user);
    }

    /**
     * Deletes a user by their unique identifier (ID).
     *
     * @param id The unique identifier of the user to delete.
     * @return True if the user was successfully deleted, false otherwise.
     * @throws EntityNotFoundException If no user with the given ID is found.
     */
    @Override
    public boolean deleteById(Long id) {
        userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User with id = " + id + " was not found"));
        return userRepository.delete(id);
    }
}
