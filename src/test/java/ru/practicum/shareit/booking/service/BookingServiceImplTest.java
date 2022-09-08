package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.Validation;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.mapper.BookingMapperImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    private BookingRepository bookingRepository;
    private UserService userService;
    private ItemService itemService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    private BookingService bookingService;

    private Validation validation = new Validation();
    private ItemMapper itemMapper = new ItemMapperImpl();
    private UserMapper userMapper = new UserMapperImpl();
    private BookingMapper bookingMapper = new BookingMapperImpl();


    private final long bookingId = 1L;
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

    private final User userOwnerForBooking = User.builder()
            .id(2L)
            .email("user2@test.ru")
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

    private final Item itemForBooking = Item.builder()
            .id(2L)
            .name("itemTest")
            .description("Test Item")
            .available("true")
            .user(userOwnerForBooking)
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
        bookingService = new BookingServiceImpl(bookingRepository, userService, itemService, validation,
                Mappers.getMapper(BookingMapper.class), Mappers.getMapper(UserMapper.class));
    }

    @DisplayName("GIVEN an user id and booking id " +
            "WHEN getBookingById method with id called " +
            "THEN findById method called at most 2 times and return booking")
    @Test
    void Test1_shouldFindBookingById() {
        when(bookingRepository.findById(anyLong()))
                .thenReturn(booking);

        BookingOutDto bookingOutDto = bookingService.getBookingById(userId, bookingId);
        bookingOutDto.setStart(LocalDateTime.of(2022, Month.JUNE, 1, 12, 00));
        bookingOutDto.setEnd(LocalDateTime.of(2022, Month.DECEMBER, 1, 12, 00));

        verify(bookingRepository, atMost(2)).findById(anyLong());
        assertEquals(bookingMapper.toOutDto(booking), bookingOutDto);
    }

    @DisplayName("GIVEN an user id and booking id " +
            "WHEN BookingById method with incorrect id called " +
            "THEN method return error")
    @Test
    void Test2_shouldReturnExceptionWhenTryFindBookingByIdWithIncorrectId() {
        when(bookingRepository.findById(anyLong()))
                .thenThrow(new NotFoundException("бронь не найдена"));

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(userId, incorrectId));
    }

    @DisplayName("GIVEN an booker id, state WAITING, from, size " +
            "WHEN getBookingsByBookerId method with id called " +
            "THEN findAllByUserIdAndStatusOrderByStartDesc method called 1 times and return list of booking")
    @Test
    void Test3_shouldFindBookingByBookerId() {
        when(bookingRepository.findAllByUserIdAndStatusOrderByStartDesc(anyLong(), any(Status.class),
                any(Pageable.class)))
                .thenReturn(List.of(booking));
        when(userRepository.findById(anyLong()))
                .thenReturn(user);

        List<BookingOutDto> bookingOutDto = bookingService.getBookingsByBookerId(userId, State.WAITING, from, size);

        for (BookingOutDto gotBookingOutDto : bookingOutDto) {
            gotBookingOutDto.setStart(LocalDateTime.of(2022, Month.JUNE, 1, 12, 00));
            gotBookingOutDto.setEnd(LocalDateTime.of(2022, Month.DECEMBER, 1, 12, 00));
        }

        verify(bookingRepository, times(1)).findAllByUserIdAndStatusOrderByStartDesc(anyLong(),
                any(Status.class), any(Pageable.class));
        assertEquals(bookingMapper.toOutDto(List.of(booking)), bookingOutDto);
    }


    @DisplayName("GIVEN an owner of item id, state CURRENT, from, size " +
            "WHEN getBookingsByBookerId method with id called " +
            "THEN findAllByUserIdAndStatusOrderByStartDesc method called 1 times and return list of booking")
    @Test
    void Test4_shouldFindBookingByOwnerId() {
        when(bookingRepository.findAllByItemUserIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));
        when(userRepository.findById(anyLong()))
                .thenReturn(user);

        List<BookingOutDto> bookingOutDto = bookingService.getBookingsByOwnerId(userId, State.CURRENT, from, size);

        for (BookingOutDto gotBookingOutDto : bookingOutDto) {
            gotBookingOutDto.setStart(LocalDateTime.of(2022, Month.JUNE, 1, 12, 00));
            gotBookingOutDto.setEnd(LocalDateTime.of(2022, Month.DECEMBER, 1, 12, 00));
        }

        verify(bookingRepository, times(1))
                .findAllByItemUserIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class),
                        any(LocalDateTime.class), any(Pageable.class));
        assertEquals(bookingMapper.toOutDto(List.of(booking)), bookingOutDto);
    }

    @DisplayName("GIVEN an user id and booking " +
            "WHEN booking saved to DB " +
            "THEN save method called 1 time and return booking")
    @Test
    void Test5_shouldCallSaveMethodWhenAddBooking() {
        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);
        when(userRepository.findById(anyLong()))
                .thenReturn(user);
        when(itemRepository.findById(anyLong()))
                .thenReturn(itemForBooking);
        when(commentRepository.findAllByItemId(anyLong()))
                .thenReturn(List.of(comment));

        BookingInDto bookingInDtoForSave = bookingMapper.toDto(booking);
        bookingInDtoForSave.setItemId(itemId);
        bookingInDtoForSave.setStart(LocalDateTime.of(2022, Month.DECEMBER, 1, 11, 00));

        BookingInDto bookingInDto = bookingService.addNewBooking(userId, bookingInDtoForSave);

        verify(bookingRepository, times(1)).save(any(Booking.class));
        assertEquals(booking.getId(), bookingMapper.toBooking(bookingInDto).getId());
        assertEquals(booking.getStatus(), bookingMapper.toBooking(bookingInDto).getStatus());
    }

    @DisplayName("GIVEN an user id of owner, booking " +
            "WHEN booking saved to DB " +
            "THEN method return error")
    @Test
    void Test6_shouldReturnExceptionWhenTryAddBookingWithOwnerId() {
        when(userRepository.findById(anyLong()))
                .thenReturn(userOwnerForBooking);
        when(itemRepository.findById(anyLong()))
                .thenReturn(itemForBooking);
        when(commentRepository.findAllByItemId(anyLong()))
                .thenReturn(List.of(comment));

        BookingInDto bookingInDtoForSave = bookingMapper.toDto(booking);
        bookingInDtoForSave.setItemId(itemId);
        bookingInDtoForSave.setStart(LocalDateTime.of(2022, Month.DECEMBER, 1, 11, 00));

        assertThrows(NotFoundException.class, () -> bookingService.addNewBooking(userOwnerForBooking.getId(),
                bookingInDtoForSave));
    }

    @DisplayName("GIVEN an user id, booking id and approved " +
            "WHEN booking updated to DB " +
            "THEN save method called 1 time and return booking")
    @Test
    void Test7_shouldCallSaveMethodWhenUpdateBooking() {
        when(bookingRepository.findById(anyLong()))
                .thenReturn(booking);
        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);

        BookingOutDto bookingOutDto = bookingService.updateBookingStatus(userId, bookingId, true);

        verify(bookingRepository, times(1)).save(any(Booking.class));
        assertEquals(bookingMapper.toOutDto(booking).getId(), bookingOutDto.getId());
        assertEquals(bookingMapper.toOutDto(booking).getStatus(), bookingOutDto.getStatus());
    }

    @DisplayName("GIVEN an user id not owner, booking id and approved " +
            "WHEN booking updated to DB " +
            "THEN method return error")
    @Test
    void Test8_shouldReturnExceptionWhenTryUpdateBooking() {
        when(bookingRepository.findById(anyLong()))
                .thenReturn(booking);

        assertThrows(NotFoundException.class, () -> bookingService.updateBookingStatus(userOwnerForBooking.getId(),
                bookingId, true));
    }
}