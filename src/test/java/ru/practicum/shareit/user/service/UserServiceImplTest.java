package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.storage.UserRepository;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserService userService;
    UserRepository userRepository;
    UserMapper userMapper;

    private final UserDto userDto = UserDto.builder()
            .id(1L)
            .email("user1@test.ru")
            .name("test")
            .build();

    @DisplayName("GIVEN an user" +
            "WHEN user saved to DB " +
            "THEN save method called 1 time")
    @Test
    void Test1_shouldCallSaveMethod() {
        when(userService.add(userDto))
                .thenReturn(userDto);

        verify(userRepository, times(1)).save(userMapper.toUser(userDto));
    }

//
//    @DisplayName("GIVEN a list of users DTO and save list to DB " +
//            "WHEN list of users called from DB " +
//            "THEN users returned")
//    @Test
//    void Test2_shouldReturnListOfAllUsers() {
//        List<UserDto> sourceUsers = List.of(
//                makeUserDto("ivan@email", "Ivan Ivanov"),
//                makeUserDto("petr@email", "Petr Petrov"),
//                makeUserDto("vasilii@email", "Vasilii Vasiliev")
//        );
//
//        for (UserDto user : sourceUsers) {
//            User entity = userMapper.toUser(user);
//            em.persist(entity);
//        }
//        em.flush();
//
//        List<UserDto> targetUsers = service.getUserAll();
//
//        assertThat(targetUsers, hasSize(sourceUsers.size()));
//        for (UserDto sourceUser : sourceUsers) {
//            assertThat(targetUsers, hasItem(allOf(
//                    hasProperty("id", notNullValue()),
//                    hasProperty("firstName", equalTo(sourceUser.getName())),
//                    hasProperty("email", equalTo(sourceUser.getEmail()))
//            )));
//        }
//    }
//
//    @Test
//    void getUserById() {
//    }
//
//
//    @Test
//    void update() {
//    }
//
//    @Test
//    void delete() {
//    }
//
//    private UserDto makeUserDto(String email, String name) {
//        UserDto dto = new UserDto();
//        dto.setEmail(email);
//        dto.setName(name);
//
//        return dto;
//    }
}