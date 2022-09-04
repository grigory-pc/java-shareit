package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.Validation;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceIntegrationlTest {
    private Validation validation = new Validation();
    private UserRepository userRepository;
    private UserService userService = new UserServiceImpl(userRepository, validation, Mappers.getMapper(UserMapper.class));

    private UserDto userDto = UserDto.builder()
            .email("user1@test.ru")
            .name("test")
            .build();

    @DisplayName("GIVEN an user dto " +
            "WHEN add new user and getUserAll method called " +
            "THEN return user dto with id 1L")
    @Test
    void Test1_shouldFindListOfAllUsers() {
        userService.add(userDto);

        List<UserDto> userDtoAll = userService.getUserAll();

        assertEquals(userDtoAll.get(0).getId(), 1L);
    }
}