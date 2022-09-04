package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.Validation;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.mapper.BookingMapperImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.CommentMapperImpl;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    private UserService userService;
    private Validation validation = new Validation();
    private ItemMapper itemMapper = new ItemMapperImpl();
    private UserMapper userMapper = new UserMapperImpl();
    private BookingMapper bookingMapper = new BookingMapperImpl();
    private CommentMapper commentMapper = new CommentMapperImpl();

    private ItemService itemService;

    private final long userId = 1L;
    private final long itemId = 1L;
    private final int from = 1;
    private final int size = 1;
    private final long incorrectId = 2L;

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

    private final Booking booking = Booking.builder()
            .id(1L)
            .start(LocalDateTime.of(2022, Month.JUNE, 1, 12, 00))
            .end(LocalDateTime.of(2022, Month.DECEMBER, 1, 12, 00))
            .status(Status.WAITING)
            .user(user)
            .item(item)
            .build();

    private final Comment comment = Comment.builder()
            .id(1L)
            .text("test comment")
            .item(item)
            .user(user)
            .created(LocalDate.of(2022, Month.JUNE, 1))
            .build();

    @BeforeEach
    void initService() {
        userService = new UserServiceImpl(userRepository, validation, Mappers.getMapper(UserMapper.class));
        itemService = new ItemServiceImpl(itemRepository, bookingRepository, commentRepository, userRepository,
                itemRequestRepository, userService, validation, Mappers.getMapper(ItemMapper.class),
                Mappers.getMapper(UserMapper.class), Mappers.getMapper(BookingMapper.class),
                Mappers.getMapper(CommentMapper.class));
    }

    @DisplayName("GIVEN an item id, from id and size " +
            "WHEN getItems method called " +
            "THEN findAllByUserId method called 1 time and return list of items")
    @Test
    void Test1_shouldFindAllItems() {
        when(itemRepository.findAllByUserId(anyLong(), any(Pageable.class)))
                .thenReturn(List.of(item));
        when(bookingRepository.findByItemIdAndItemUserIdAndEndIsBeforeOrderByStart(anyLong(), anyLong(),
                any(LocalDateTime.class)))
                .thenReturn(booking);
        when(bookingRepository.findByItemIdAndItemUserIdAndStartIsAfterOrderByStart(anyLong(), anyLong(),
                any(LocalDateTime.class)))
                .thenReturn(booking);

        List<ItemDto> itemsDto = itemService.getItems(userId, from, size);

        for (ItemDto itemDto : itemsDto) {
            itemDto.setLastBooking(null);
            itemDto.setNextBooking(null);
        }
        verify(itemRepository, times(1)).findAllByUserId(anyLong(), any(Pageable.class));
        assertEquals(List.of(itemMapper.toDto(item)), itemsDto);
    }

    @DisplayName("GIVEN an user id and item id " +
            "WHEN getItemDtoById method with id called " +
            "THEN findById method called at most 2 times and return item")
    @Test
    void Test2_shouldFindItemById() {
        when(itemRepository.findById(anyLong()))
                .thenReturn(item);
        when(commentRepository.findAllByItemId(anyLong()))
                .thenReturn(List.of(comment));

        ItemDto itemDto = itemService.getItemDtoById(userId, itemId);
        itemDto.setComments(null);

        verify(itemRepository, atMost(2)).findById(anyLong());
        assertEquals(itemMapper.toDto(item), itemDto);
    }

    @DisplayName("GIVEN an user id and incorrect item id " +
            "WHEN getItemDtoById method with incorrect id called " +
            "THEN method return error")
    @Test
    void Test3_shouldReturnExceptionWhenFindItemByIncorrectId() {
        when(itemRepository.findById(anyLong()))
                .thenThrow(new NotFoundException("Вещь не найдена"));

        assertThrows(NotFoundException.class, () -> itemService.getItemDtoById(userId, incorrectId));
    }

    @DisplayName("GIVEN an item " +
            "WHEN item saved to DB " +
            "THEN save method called 1 time and return item")
    @Test
    void Test4_shouldCallSaveMethodWhenAddItem() {
        when(itemRepository.save(any(Item.class)))
                .thenReturn(item);
        when(userRepository.findById(anyLong()))
                .thenReturn(user);
        when(itemRequestRepository.findById(anyLong()))
                .thenReturn(itemRequest);

        ItemShortDto itemShortDto = itemService.addNewItem(userId, itemMapper.toShortDto(item));

        verify(itemRepository, times(1)).save(any(Item.class));
        assertEquals(item.getId(), itemMapper.toItem(itemShortDto).getId());
        assertEquals(item.getDescription(), itemMapper.toItem(itemShortDto).getDescription());
        assertEquals(item.getAvailable(), itemMapper.toItem(itemShortDto).getAvailable());
    }

    @DisplayName("GIVEN an item, item id and user id " +
            "WHEN item updated to DB " +
            "THEN save method called 1 time and return item")
    @Test
    void Test5_shouldCallSaveMethodWhenUpdateItem() {
        when(itemRepository.save(any(Item.class)))
                .thenReturn(item);
        when(itemRepository.findAllByIdAndUserId(anyLong(), anyLong()))
                .thenReturn(List.of(item));
        when(itemRepository.findById(anyLong()))
                .thenReturn(item);
        when(userRepository.findById(anyLong()))
                .thenReturn(user);

        ItemShortDto itemShortDto = itemService.updateItem(userId, itemId, itemMapper.toShortDto(item));

        verify(itemRepository, times(1)).save(any(Item.class));
        assertEquals(item.getId(), itemMapper.toItem(itemShortDto).getId());
        assertEquals(item.getDescription(), itemMapper.toItem(itemShortDto).getDescription());
        assertEquals(item.getAvailable(), itemMapper.toItem(itemShortDto).getAvailable());
    }

    @DisplayName("GIVEN an user id and incorrect item id " +
            "WHEN updateItem method with incorrect id called " +
            "THEN method return error")
    @Test
    void Test6_shouldReturnExceptionWhenUpdateItemWithIncorrectId() {
        when(itemRepository.findAllByIdAndUserId(anyLong(), anyLong()))
                .thenThrow(new NotFoundException("Вещь с id для данного пользователя не найдена"));
        when(userRepository.findById(anyLong()))
                .thenReturn(user);

        assertThrows(NotFoundException.class, () -> itemService.updateItem(userId, incorrectId,
                itemMapper.toShortDto(item)));
    }

    @DisplayName("GIVEN an user id, item id and comment " +
            "WHEN comment saved to DB " +
            "THEN save method called 1 time and return comment")
    @Test
    void Test7_shouldCallSaveMethodWhenAddComment() {
        when(commentRepository.save(any(Comment.class)))
                .thenReturn(comment);
        when(bookingRepository.findByUserIdAndItemIdAndEndIsBefore(anyLong(), anyLong(),
                any(LocalDateTime.class)))
                .thenReturn(booking);
        when(userRepository.findById(anyLong()))
                .thenReturn(user);
        when(itemRepository.findById(anyLong()))
                .thenReturn(item);

        CommentDto commentDto = itemService.addComment(userId, itemId, commentMapper.toDto(comment));

        verify(commentRepository, times(1)).save(any(Comment.class));
        assertEquals(comment.getId(), commentMapper.toComment(commentDto).getId());
        assertEquals(comment.getText(), commentMapper.toComment(commentDto).getText());
    }


    @DisplayName("GIVEN an item id" +
            "WHEN try delete item from DB " +
            "THEN delete method called 1 time")
    @Test
    void Test8_shouldCallDeleteMethodWhenDeleteUser() {
        itemService.deleteItem(userId, itemId);

        verify(itemRepository, times(1)).deleteById(anyLong());
    }

    @DisplayName("GIVEN an text for search, from id and size " +
            "WHEN searchItemByText method with text called " +
            "THEN findAllByAvailableAndDescriptionContainingIgnoreCase method called 1 times and return item")
    @Test
    void Test9_shouldFindItemByText() {
        when(itemRepository.findAllByAvailableAndDescriptionContainingIgnoreCase(anyString(), anyString(),
                any(Pageable.class)))
                .thenReturn(List.of(item));

        List<ItemShortDto> itemShortDto = itemService.searchItemByText(item.getDescription(), from, size);

        verify(itemRepository, times(1))
                .findAllByAvailableAndDescriptionContainingIgnoreCase(anyString(), anyString(), any(Pageable.class));
        assertEquals(itemMapper.toShortDto(List.of(item)), itemShortDto);
    }
}