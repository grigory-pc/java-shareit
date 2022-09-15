package ru.practicum.shareit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.Validation;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.mapper.ItemRequestMapper;
import ru.practicum.shareit.requests.mapper.ItemRequestMapperImpl;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.service.ItemRequestService;
import ru.practicum.shareit.requests.service.ItemRequestServiceImpl;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private ItemRequestMapper itemRequestMapper = new ItemRequestMapperImpl();

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
            .available("true")
            .user(user)
            .itemRequest(itemRequest)
            .build();

    private Long userId;
    private Long requestId;

    @BeforeEach
    public void initService() {
        userService = new UserServiceImpl(userRepository, Mappers.getMapper(UserMapper.class));
        itemRequestService = new ItemRequestServiceImpl(Mappers.getMapper(ItemRequestMapper.class),
                Mappers.getMapper(ItemMapper.class), itemRequestRepository, userRepository, itemRepository, userService);

        userId = 1L;
        requestId = 1L;
    }

    @DisplayName("GIVEN an user id " +
            "WHEN getItemRequestByUserId method with id called " +
            "THEN findById method called 1 time and return list of requests")
    @Test
    void Test1_shouldFindItemRequestByUserId() {
        when(userRepository.findById(anyLong()))
                .thenReturn(user);
        when(itemRequestRepository.findAllByUserIdOrderByCreatedDesc(anyLong()))
                .thenReturn(List.of(itemRequest));

        List<ItemRequestDto> itemRequestAll = itemRequestService.getItemRequestByUserId(userId);

        for (ItemRequestDto itemRequestDto : itemRequestAll) {
            itemRequestDto.setItems(null);
        }

        verify(itemRequestRepository, times(1)).findAllByUserIdOrderByCreatedDesc(anyLong());
        assertEquals(List.of(itemRequestMapper.toDto(itemRequest)), itemRequestAll);
    }

    @DisplayName("GIVEN an user id, from id and size " +
            "WHEN getAllItemRequest method called " +
            "THEN findAllByUserIdIsNot method called 1 time and return list of requests")
    @Test
    void Test2_shouldFindAllItemRequest() {
        when(itemRepository.findAllByItemRequestId(anyLong()))
                .thenReturn(List.of(item));
        when(itemRequestRepository.findAllByUserIdIsNot(anyLong(), any(Pageable.class)))
                .thenReturn(List.of(itemRequest));

        List<ItemRequestDto> itemRequestAll = itemRequestService.getAllItemRequest(userId, 1, 1);

        for (ItemRequestDto itemRequestDto : itemRequestAll) {
            itemRequestDto.setItems(null);
        }

        verify(itemRequestRepository, times(1)).findAllByUserIdIsNot(anyLong(), any(Pageable.class));
        assertEquals(List.of(itemRequestMapper.toDto(itemRequest)), itemRequestAll);
    }

    @DisplayName("GIVEN a request id " +
            "WHEN getItemRequestById method with id called " +
            "THEN findAllByItemRequestId method called at most 2 times and return request")
    @Test
    void Test3_shouldFindItemRequestById() {
        when(userRepository.findById(anyLong()))
                .thenReturn(user);
        when(itemRepository.findAllByItemRequestId(anyLong()))
                .thenReturn(new ArrayList<>());
        when(itemRequestRepository.findById(anyLong()))
                .thenReturn(itemRequest);

        ItemRequestDto itemRequestDto = itemRequestService.getItemRequestById(userId, requestId);

        itemRequestDto.setItems(null);

        verify(itemRequestRepository, atMost(2)).findById(anyLong());
        assertEquals(itemRequestMapper.toDto(itemRequest), itemRequestDto);
    }

    @DisplayName("GIVEN a request id " +
            "WHEN getItemRequestById method with incorrect id called " +
            "THEN return error")
    @Test
    void Test4_shouldReturnExceptionWhenFindRequestByIncorrectId() {
        lenient().when(itemRequestRepository.findById(anyLong()))
                .thenThrow(new NotFoundException("Пользователь не найден"));

        assertThrows(NotFoundException.class, () -> itemRequestService.getItemRequestById(1L, 2L));
    }

    @DisplayName("GIVEN a request " +
            "WHEN request saved to DB " +
            "THEN save method called 1 time and return request")
    @Test
    void Test5_shouldCallSaveMethodWhenAddItemRequest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(user);
        when(itemRequestRepository.save(any(ItemRequest.class)))
                .thenReturn(itemRequest);

        ItemRequestDto itemRequestDto = itemRequestService.addItemRequest(userId, itemRequestMapper.toDto(itemRequest));

        verify(itemRequestRepository, times(1)).save(any(ItemRequest.class));
        assertEquals(itemRequest.getId(), itemRequestMapper.toItemRequest(itemRequestDto).getId());
        assertEquals(itemRequest.getDescription(), itemRequestMapper.toItemRequest(itemRequestDto).getDescription());
    }
}