package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.Validation;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    private Validation validation = new Validation();
    private UserMapper userMapper = new UserMapperImpl();
    private UserService userService;

    private User user = User.builder()
            .id(1L)
            .email("user1@test.ru")
            .name("test")
            .build();

    private long id;
    private long incorrectId;

    @BeforeEach
    public void initService() {
        userService = new UserServiceImpl(userRepository, validation, Mappers.getMapper(UserMapper.class));

        id = 1L;
        incorrectId = 2L;
    }

    @DisplayName("GIVEN an user " +
            "WHEN user saved to DB " +
            "THEN save method called 1 time and return user")
    @Test
    void Test1_shouldCallSaveMethodWhenAddUser() {
        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserDto userDto = userService.add(userMapper.toDto(user));

        verify(userRepository, times(1)).save(user);
        assertEquals(user, userMapper.toUser(userDto));
    }

    @DisplayName("GIVEN an user id " +
            "WHEN getUserById method with id called " +
            "THEN findById method called at most 2 times and return user")
    @Test
    void Test2_shouldFindUserById() {
        when(userRepository.findById(anyLong()))
                .thenReturn(user);

        UserDto userDto = userService.getUserById(id);

        verify(userRepository, atMost(2)).findById(anyLong());
        assertEquals(user, userMapper.toUser(userDto));
    }

    @DisplayName("GIVEN an user id " +
            "WHEN getUserById method with incorrect id called " +
            "THEN return error")
    @Test
    void Test3_shouldReturnExceptionWhenFindUserByIncorrectId() {
        when(userRepository.findById(anyLong()))
                .thenThrow(new NotFoundException("Пользователь не найден"));

        assertThrows(NotFoundException.class, () -> userService.getUserById(incorrectId));
    }

    @DisplayName("GIVEN an user and user id " +
            "WHEN user updated to DB " +
            "THEN save method called 1 time and return user")
    @Test
    void Test4_shouldCallSaveMethodWhenUpdateUser() {
        when(userRepository.save(any(User.class)))
                .thenReturn(user);
        when(userRepository.findById(anyLong()))
                .thenReturn(user);

        UserDto userDto = userService.update(id, userMapper.toDto(user));

        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).findById(anyLong());
        assertEquals(user, userMapper.toUser(userDto));
    }

    @DisplayName("GIVEN an user id " +
            "WHEN try delete user from DB " +
            "THEN delete method called 1 time")
    @Test
    void Test5_shouldCallDeleteMethodWhenDeleteUser() {
        userService.delete(id);

        verify(userRepository, times(1)).deleteById(anyLong());
    }

    @DisplayName("GIVEN " +
            "WHEN getUserAll method called " +
            "THEN findAll method called 1 time and return List of Users")
    @Test
    void Test6_shouldFindListOfAllUsers() {
        when(userRepository.findAll())
                .thenReturn(List.of(user));

        List<UserDto> userDtoAll = userService.getUserAll();

        verify(userRepository, times(1)).findAll();
        assertEquals(List.of(userMapper.toDto(user)), userDtoAll);
    }
}