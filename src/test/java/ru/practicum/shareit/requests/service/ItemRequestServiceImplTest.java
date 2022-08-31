package ru.practicum.shareit.requests.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.Validation;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.mapper.ItemRequestMapper;
import ru.practicum.shareit.requests.mapper.ItemRequestMapperImpl;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.storage.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    private Validation validation = new Validation();
    ;
    private ItemRequestMapper itemRequestMapper = new ItemRequestMapperImpl();
    private ItemMapper itemMapper = new ItemMapperImpl();
    private UserMapper userMapper = new UserMapperImpl();


    @Test
    void getItemRequestByUserId() {
        UserService userService = new UserServiceImpl(userRepository, validation, userMapper);
        ItemRequestService itemRequestService = new ItemRequestServiceImpl(validation, itemRequestMapper,itemMapper,
                itemRequestRepository, userRepository, itemRepository, userService);


    }

    @Test
    void getAllItemRequest() {
    }

    @Test
    void getItemRequestById() {
    }

    @Test
    void addItemRequest() {
    }


    private final User user = User.builder()
            .id(1L)
            .email("user1@test.ru")
            .name("test")
            .build();

//    @BeforeEach
//    public void initService(){
//
//    }

    @DisplayName("GIVEN an user" +
            "WHEN user saved to DB " +
            "THEN save method called 1 time and return user")
    @Test
    void Test1_shouldCallSaveMethodWhenAddUser() {
        UserService userService = new UserServiceImpl(userRepository, validation, userMapper);

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
        UserService userService = new UserServiceImpl(userRepository, validation, userMapper);

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
        UserService userService = new UserServiceImpl(userRepository, validation, userMapper);

        when(userRepository.findById(2L))
                .thenThrow(new NotFoundException("Пользователь не найден"));

        assertThrows(NotFoundException.class, () -> userService.getUserById(2L));
    }

    @DisplayName("GIVEN an user and user id" +
            "WHEN user updated to DB " +
            "THEN save method called 1 time and return user")
    @Test
    void Test4_shouldCallSaveMethodWhenUpdateUser() {
        UserService userService = new UserServiceImpl(userRepository, validation, userMapper);

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
        UserService userService = new UserServiceImpl(userRepository, validation, userMapper);

        userService.delete(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }


}