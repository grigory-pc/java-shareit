package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Validation;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * Класс, ответственный за операции с пользователями
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Validation validation;
    private final UserMapper userMapper;

    /**
     * Возвращает список всех пользователей
     */
    @Override
    public List<UserDto> getUserAll() {
        List<User> users = userRepository.findAll();

        return userMapper.toDto(users);
    }

    /**
     * Валидирует id и возвращает пользователя по ID
     */
    @Override
    public UserDto getUserById(long userId) {
        validation.validationId(userId);
        if (userRepository.findById(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return userMapper.toDto(userRepository.findById(userId));
    }

    /**
     * Добавляет пользователя
     */
    @Override
    @Transactional
    public UserDto add(UserDto userDto) {
        User newUser = userRepository.save(userMapper.toUser(userDto));

        return userMapper.toDto(newUser);
    }

    /**
     * Валидирует id и обновляет пользователя
     */
    @Override
    @Transactional
    public UserDto update(long userId, UserDto userDto) {
        validation.validationId(userId);

        userDto.setId(userId);

        User userForUpdate = userRepository.findById(userId);

        userMapper.updateUserFromDto(userDto, userForUpdate);

        User updatedUser = userRepository.save(userForUpdate);

        return userMapper.toDto(updatedUser);
    }

    /**
     * Удаление пользователя
     */
    @Override
    @Transactional
    public void delete(long userId) {
        validation.validationId(userId);

        userRepository.deleteById(userId);
    }
}