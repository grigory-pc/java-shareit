package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Возвращает список всех пользователей
     */
    @Override
    public List<UserDto> getUserAll() {
        log.info("Получен запрос на поиск всех пользователей");

        List<User> users = userRepository.findAll();

        return userMapper.toDto(users);
    }

    /**
     * Валидирует id и возвращает пользователя по ID
     */
    @Override
    public UserDto getUserById(long userId) {
        log.info("Получен запрос для пользователя " + userId);

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
        log.info("Получен запрос на добавление пользователя " + userDto.getEmail());

        User newUser = userRepository.save(userMapper.toUser(userDto));

        return userMapper.toDto(newUser);
    }

    /**
     * Валидирует id и обновляет пользователя
     */
    @Override
    @Transactional
    public UserDto update(long userId, UserDto userDto) {
        log.info("Получен запрос на обновление пользователя с id = " + userId);

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
        log.info("Получен запрос на удаление пользователя c id = " + userId);

        userRepository.deleteById(userId);
    }
}