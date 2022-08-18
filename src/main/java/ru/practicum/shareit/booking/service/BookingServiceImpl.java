package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Validation;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс, ответственный за операции с бронированием
 */
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;
    @Autowired
    private Validation validation;
    @Autowired
    private BookingMapper bookingMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, UserService userService, ItemService itemService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.itemService = itemService;
    }

    /**
     * Возвращает список всех бронирований
     */
    @Override
    public List<BookingDto> getBookings(long userId) {
        List<Booking> bookings = bookingRepository.findAll().stream()
                .filter(user -> user.getId() == userId)
                .collect(Collectors.toList());
        return bookingMapper.toDto(bookings);
    }

    /**
     * Возвращает бронь по ID
     */
    @Override
    public BookingDto getBookingById(long userId, long bookingId) {
        validation.validationId(userId);
        validation.validationId(bookingId);

        if (bookingRepository.findById(bookingId) == null) {
            throw new NotFoundException("бронь не найдена");
        }
        Booking existBooking = bookingRepository.findById(bookingId);

        if ((existBooking.getUser().getId() == userId) || (existBooking.getItem().getId() == userId)) {
            return bookingMapper.toDto(existBooking);
        } else {
            throw new NotFoundException("бронь не найдена");
        }
    }

    /**
     * Возвращает все брони пользователя по его userId
     */
    @Override
    public List<BookingDto> getBookingsByBookerId(long userId, State state) {
        validation.validationId(userId);

        List<Booking> allBookings = bookingRepository.findAllByUserId(userId);

        return getBookingsFilteredByState(allBookings, state);
    }

    /**
     * Возвращает все брони владельца вещей по его userId
     */
    @Override
    public List<BookingDto> getBookingsByOwnerId(long userId, State state) {
        validation.validationId(userId);

        List<Booking> existBookingsByOwnerId = new ArrayList<>();

        List<ItemDto> existItemsOfUser = itemService.getItems(userId);

        for (ItemDto existItemDto : existItemsOfUser) {
            existBookingsByOwnerId.add(bookingRepository.findAllByItemId(existItemDto.getId()));
        }

        existBookingsByOwnerId.stream()
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .collect(Collectors.toList());

        return bookingMapper.toDto(existBookingsByOwnerId);
    }

    /**
     * Добавляет бронь
     */
    @Override
    public BookingDto addNewBooking(long userId, BookingDto bookingDto) {
        validation.validationId(userId);
        validation.validationId(bookingDto.getItemId());

        Booking bookingForSave = bookingMapper.toBooking(bookingDto);
        bookingForSave.setUser(getExistUser(userId));
        bookingForSave.setItem(getExistItem(bookingDto.getItemId()));
        bookingForSave.setStatus(Status.WAITING);

        return bookingMapper.toDto(bookingRepository.save(bookingForSave));
    }

    /**
     * Обновляет бронь
     */
    @Override
    public BookingDto updateBooking(long userId, long bookingId, boolean approved, BookingDto bookingDto) {
        validation.validationId(userId);
        validation.validationId(bookingDto.getItemId());
        validation.validationId(bookingId);

        userService.getUserById(userId);
        itemService.getItemById(bookingDto.getItemId());

        Booking bookingForUpdate = bookingRepository.findById(bookingId);

        bookingMapper.updateBookingFromDto(bookingDto, bookingForUpdate);

        bookingForUpdate.setUser(getExistUser(userId));
        bookingForUpdate.setItem(getExistItem(bookingDto.getItemId()));
        if (approved) {
            bookingForUpdate.setStatus(Status.APPROVED);
        } else {
            bookingForUpdate.setStatus(Status.REJECTED);
        }

        Booking updatedBooking = bookingRepository.save(bookingForUpdate);

        return bookingMapper.toDto(updatedBooking);
    }

    /**
     * Удаляет бронь
     */
    @Override
    public void deleteBooking(long userId, long bookingId) {
        validation.validationId(userId);
        validation.validationId(bookingId);

        bookingRepository.deleteById(bookingId);
    }

    private User getExistUser(long userId) {
        return userMapper.toUser(userService.getUserById(userId));
    }

    private Item getExistItem(long itemId) {
        return itemMapper.toItem(itemService.getItemById(itemId));
    }

    List<BookingDto> getBookingsFilteredByState(List<Booking> allBookings, State state) {
        switch (state) {
            case ALL:
                return bookingMapper.toDto(allBookings);
            case CURRENT:
                return bookingMapper.toDto(allBookings.stream()
                        .filter(booking -> LocalDateTime.now().isAfter(booking.getStart())
                                && LocalDateTime.now().isBefore(booking.getEnd()))
                        .collect(Collectors.toList())
                );
            case FUTURE:
                bookingMapper.toDto(allBookings.stream()
                        .filter(booking -> LocalDateTime.now().isBefore(booking.getStart()))
                        .collect(Collectors.toList())
                );
            case PAST:
                bookingMapper.toDto(allBookings.stream()
                        .filter(booking -> LocalDateTime.now().isAfter(booking.getEnd()))
                        .collect(Collectors.toList())
                );
            case REJECTED:
                bookingMapper.toDto(allBookings.stream()
                        .filter(booking -> booking.getStatus().equals(Status.REJECTED))
                        .collect(Collectors.toList())
                );
            case WAITING:
                bookingMapper.toDto(allBookings.stream()
                        .filter(booking -> booking.getStatus().equals(Status.WAITING))
                        .collect(Collectors.toList())
                );
            default:
                throw new ValidationException("Некорректный статус");
        }
    }
}