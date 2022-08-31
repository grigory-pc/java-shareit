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

    @BeforeEach
    public void initService() {
        userService = new UserServiceImpl(userRepository, validation, Mappers.getMapper(UserMapper.class));
    }

    @DisplayName("GIVEN an user" +
            "WHEN user saved to DB " +
            "THEN save method called 1 time and return user")
    @Test
    void Test1_shouldCallSaveMethodWhenAddUser() {
        when(userRepository.save(user))
                .thenReturn(user);

        UserDto userDto = userService.add(userMapper.toDto(user));

        verify(userRepository, times(1)).save(user);
        assertEquals(user, userMapper.toUser(userDto));
    }

    @DisplayName("GIVEN an user id" +
            "WHEN getUserById method with id=1L called " +
            "THEN findById method called at most 2 times and return user")
    @Test
    void Test2_shouldFindUserById() {
        when(userRepository.findById(1L))
                .thenReturn(user);

        UserDto userDto = userService.getUserById(1L);

        verify(userRepository, atMost(2)).findById(1L);
        assertEquals(user, userMapper.toUser(userDto));
    }

    @DisplayName("GIVEN an user id" +
            "WHEN getUserById method with id=2L called " +
            "THEN findById method called at most 2 times and return error")
    @Test
    void Test3_shouldReturnExceptionWhenFindUserByWrongId() {
        when(userRepository.findById(2L))
                .thenThrow(new NotFoundException("Пользователь не найден"));

        assertThrows(NotFoundException.class, () -> userService.getUserById(2L));
    }

    @DisplayName("GIVEN an user and user id" +
            "WHEN user updated to DB " +
            "THEN save method called 1 time and return user")
    @Test
    void Test4_shouldCallSaveMethodWhenUpdateUser() {
        when(userRepository.save(user))
                .thenReturn(user);
        when(userRepository.findById(1L))
                .thenReturn(user);

        UserDto userDto = userService.update(1, userMapper.toDto(user));

        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).findById(1L);
        assertEquals(user, userMapper.toUser(userDto));
    }

    @DisplayName("GIVEN an user id" +
            "WHEN user updated to DB " +
            "THEN delete method called 1 time")
    @Test
    void Test5_shouldCallDeleteMethodWhenDeleteUser() {
        userService.delete(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}