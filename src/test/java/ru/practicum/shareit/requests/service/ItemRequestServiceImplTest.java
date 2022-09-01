package ru.practicum.shareit.requests.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.OffsetBasedPageRequest;
import ru.practicum.shareit.Validation;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.mapper.ItemRequestMapper;
import ru.practicum.shareit.requests.mapper.ItemRequestMapperImpl;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private ItemRequestMapper itemRequestMapper = new ItemRequestMapperImpl();
    private ItemMapper itemMapper = new ItemMapperImpl();
    private UserMapper userMapper = new UserMapperImpl();

    private UserService userService;
    private ItemRequestService itemRequestService;

    private final User user = User.builder()
            .id(1L)
            .email("user1@test.ru")
            .name("test")
            .build();

    private final ItemRequest itemRequest = ItemRequest.builder()
            .id(1L)
            .description("Test request")
            .created(LocalDateTime.now())
            .user(user)
            .build();

    private final Item item = Item.builder()
            .id(1L)
            .name("itemTest")
            .description("Test Item")
            .available("True")
            .user(user)
            .itemRequest(itemRequest)
            .build();

    private Long userId;
    private Long requestId;
    private Long incorrectId;

    @Mock
    private OffsetBasedPageRequest pageable;

    @BeforeEach
    public void initService() {
        userService = new UserServiceImpl(userRepository, validation, Mappers.getMapper(UserMapper.class));
        itemRequestService = new ItemRequestServiceImpl(validation, Mappers.getMapper(ItemRequestMapper.class),
                Mappers.getMapper(ItemMapper.class), itemRequestRepository, userRepository, itemRepository, userService);

        userId = 1L;
        requestId = 1L;
        incorrectId = 2L;
    }

    @DisplayName("GIVEN an user id" +
            "WHEN getItemRequestByUserId method with id called " +
            "THEN findById method called 1 time and return list of requests")
    @Test
    void Test1_shouldFindItemRequestByUserId() {
        when(userRepository.findById(1L))
                .thenReturn(user);
        when(itemRequestRepository.findAllByUserIdOrderByCreatedDesc(userId))
                .thenReturn(List.of(itemRequest));

        List<ItemRequestDto> itemRequestAll = itemRequestService.getItemRequestByUserId(userId);

        verify(itemRequestRepository, times(1)).findAllByUserIdOrderByCreatedDesc(userId);
        assertEquals(List.of(itemRequestMapper.toDto(itemRequest)), itemRequestAll);
    }

    @DisplayName("GIVEN an user id, from id and size" +
            "WHEN getAllItemRequest method called " +
            "THEN findAllByUserIdIsNot method called 1 time and return list of requests")
    @Test
    void Test2_shouldFindAllItemRequest() {
        pageable = (OffsetBasedPageRequest) OffsetBasedPageRequest.of(1, 1, Sort.by(Sort.Direction.DESC, "created"));

        when(pageable.getOffset())
                .thenReturn(1L);
        when(pageable.getPageSize())
                .thenReturn(1);
        when(pageable.getSort())
                .thenReturn(Sort.by(Sort.Direction.DESC, "created"));
        when(itemRepository.findAllByItemRequestId(1L))
                .thenReturn(List.of(item));
        when(itemRequestRepository.findAllByUserIdIsNot(1L, pageable))
                .thenReturn(List.of(itemRequest));

        List<ItemRequestDto> itemRequestAll = itemRequestService.getAllItemRequest(1L, 1, 1);


        verify(itemRequestRepository, times(1)).findAllByUserIdIsNot(1L, pageable);
        assertEquals(List.of(itemRequestMapper.toDto(itemRequest)), itemRequestAll);
    }

    @DisplayName("GIVEN an request id" +
            "WHEN getItemRequestById method with id called " +
            "THEN findAllByItemRequestId method called 1 time and return request")
    @Test
    void Test3_shouldFindItemRequestById() {
        when(userRepository.findById(1L))
                .thenReturn(user);
        when(itemRepository.findAllByItemRequestId(requestId))
                .thenReturn(new ArrayList<>());
        when(itemRequestRepository.findById(1L))
                .thenReturn(itemRequest);

        ItemRequestDto itemRequestDto = itemRequestService.getItemRequestById(1L, 1L);

        verify(itemRequestRepository, times(2)).findById(1L);
        assertEquals(itemRequestMapper.toDto(itemRequest), itemRequestDto);
    }

    @DisplayName("GIVEN an request id" +
            "WHEN getItemRequestById method with incorrect id called " +
            "THEN findById method called at most 2 times and return error")
    @Test
    void Test4_shouldReturnExceptionWhenFindRequestByIncorrectId() {
        lenient().when(itemRequestRepository.findById(2L))
                .thenThrow(new NotFoundException("Пользователь не найден"));

        assertThrows(NotFoundException.class, () -> itemRequestService.getItemRequestById(1L, 2L));
    }

    @DisplayName("GIVEN an request" +
            "WHEN request saved to DB " +
            "THEN save method called 1 time and return item request")
    @Test
    void Test5_shouldCallSaveMethodWhenAddItemRequest() {
        when(userRepository.findById(1L))
                .thenReturn(user);
        when(itemRequestRepository.save(itemRequest))
                .thenReturn(itemRequest);

        ItemRequestDto itemRequestDto = itemRequestService.addItemRequest(1L, itemRequestMapper.toDto(itemRequest));

        verify(itemRequestRepository, times(1)).save(itemRequest);
        assertEquals(itemRequest.getId(), itemRequestMapper.toItemRequest(itemRequestDto).getId());
        assertEquals(itemRequest.getDescription(), itemRequestMapper.toItemRequest(itemRequestDto).getDescription());
    }
}