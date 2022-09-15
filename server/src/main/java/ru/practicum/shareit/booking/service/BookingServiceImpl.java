package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.OffsetBasedPageRequest;
import ru.practicum.shareit.Validation;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.StateException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс, ответственный за операции с бронированием
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;
    private final Validation validation;
    private final BookingMapper bookingMapper;
    private final UserMapper userMapper;

    /**
     * Возвращает бронь по ID
     */
    @Override
    public BookingOutDto getBookingById(long userId, long bookingId) {
        log.info("Получен запрос для пользователя " + userId + " и id бронирования " + bookingId);

        if (bookingRepository.findById(bookingId) == null) {
            throw new NotFoundException("бронь не найдена");
        }
        Booking existBooking = bookingRepository.findById(bookingId);

        if ((existBooking.getUser().getId() == userId) || (existBooking.getItem().getUser().getId() == userId)) {
            return bookingMapper.toOutDto(existBooking);
        } else {
            throw new NotFoundException("бронь не найдена");
        }
    }

    /**
     * Возвращает все брони пользователя по его userId
     */
    @Override
    public List<BookingOutDto> getBookingsByBookerId(long userId, State state, int from, int size) {
        log.info("Получен запрос для пользователя " + userId + " и статуса  " + state);

        userService.getUserById(userId);

        return getBookingsForBookerFilteredByState(userId, state, from, size);
    }

    /**
     * Возвращает все брони владельца вещей по его userId
     */
    @Override
    public List<BookingOutDto> getBookingsByOwnerId(long userId, State state, int from, int size) {
        log.info("Получен запрос для пользователя " + userId + " и статуса  " + state);

        userService.getUserById(userId);

        return getBookingsForOwnerFilteredByState(userId, state, from, size);
    }

    /**
     * Добавляет бронь
     */
    @Override
    @Transactional
    public BookingInDto addNewBooking(long userId, BookingInDto bookingInDto) {
        log.info("Получен запрос на добавление бронирования для пользователя " + userId
                + " и нового бронирования для вещи " + bookingInDto.getItemName());

        validation.validationId(bookingInDto.getItemId());
        validateDateTimeOfBooking(bookingInDto);

        if (itemService.getItemDtoById(userId, bookingInDto.getItemId()).getAvailable().equals("false")) {
            throw new ValidationException("Вещь недоступна для бронирования");
        }

        Booking bookingForSave = bookingMapper.toBooking(bookingInDto);

        bookingForSave.setUser(getExistUser(userId));
        bookingForSave.setItem(getExistItem(bookingInDto.getItemId()));

        if (bookingForSave.getItem().getUser().getId() == userId) {
            throw new NotFoundException("Владелец вещи не может забронировать собственную вещь");
        }

        bookingForSave.setStatus(Status.WAITING);

        return bookingMapper.toDto(bookingRepository.save(bookingForSave));
    }

    /**
     * Обновляет статус брони
     */
    @Override
    @Transactional
    public BookingOutDto updateBookingStatus(long userId, long bookingId, boolean approved) {
        log.info("Получен запрос на обновление статуса бронирования для пользователя " + userId + " и подтверждение = "
                + approved);

        Booking bookingForUpdate = bookingRepository.findById(bookingId);

        if (bookingForUpdate.getStatus().equals(Status.APPROVED) && approved) {
            throw new ValidationException("бронь уже подтверждена");
        }

        if (bookingForUpdate.getItem().getUser().getId() == userId) {
            if (approved) {
                bookingForUpdate.setStatus(Status.APPROVED);
            } else {
                bookingForUpdate.setStatus(Status.REJECTED);
            }
        } else {
            throw new NotFoundException("Вы не являетесь владельцем вещи");
        }

        Booking updatedBooking = bookingRepository.save(bookingForUpdate);

        return bookingMapper.toOutDto(updatedBooking);
    }

    private User getExistUser(long userId) {
        return userMapper.toUser(userService.getUserById(userId));
    }

    private Item getExistItem(long itemId) {
        return itemService.getItemById(itemId);
    }

    List<BookingOutDto> getBookingsForBookerFilteredByState(long userId, State state, int from, int size) {
        Pageable pageable = OffsetBasedPageRequest.of(from, size);

        switch (state) {
            case ALL:
                return bookingMapper.toOutDto(bookingRepository.findAllByUserId_OrderByStartDesc(userId, pageable));
            case CURRENT:
                return bookingMapper.toOutDto(bookingRepository.
                        findAllByUserIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId, LocalDateTime.now(),
                                LocalDateTime.now(), pageable));
            case FUTURE:
                return bookingMapper.toOutDto(bookingRepository.findAllByUserIdAndStartIsAfterOrderByStartDesc(userId,
                        LocalDateTime.now(), pageable));
            case PAST:
                return bookingMapper.toOutDto(bookingRepository.findAllByUserIdAndEndIsBeforeOrderByStartDesc(userId,
                        LocalDateTime.now(), pageable));
            case REJECTED:
                return bookingMapper.toOutDto(bookingRepository.findAllByUserIdAndStatusOrderByStartDesc(userId,
                        Status.REJECTED, pageable));
            case WAITING:
                return bookingMapper.toOutDto(bookingRepository.findAllByUserIdAndStatusOrderByStartDesc(userId,
                        Status.WAITING, pageable));
            default:
                throw new StateException();
        }
    }

    List<BookingOutDto> getBookingsForOwnerFilteredByState(long userId, State state, int from, int size) {
        Pageable pageable = OffsetBasedPageRequest.of(from, size);

        switch (state) {
            case ALL:
                return bookingMapper.toOutDto(bookingRepository.findAllByItemUserId_OrderByStartDesc(userId, pageable));
            case CURRENT:
                return bookingMapper.toOutDto(bookingRepository.
                        findAllByItemUserIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId, LocalDateTime.now(),
                                LocalDateTime.now(), pageable));
            case FUTURE:
                return bookingMapper.toOutDto(bookingRepository.findAllByItemUserIdAndStartIsAfterOrderByStartDesc(userId,
                        LocalDateTime.now(), pageable));
            case PAST:
                return bookingMapper.toOutDto(bookingRepository.findAllByItemUserIdAndEndIsBeforeOrderByStartDesc(userId,
                        LocalDateTime.now(), pageable));
            case REJECTED:
                return bookingMapper.toOutDto(bookingRepository.findAllByItemUserIdAndStatusOrderByStartDesc(userId,
                        Status.REJECTED, pageable));
            case WAITING:
                return bookingMapper.toOutDto(bookingRepository.findAllByItemUserIdAndStatusOrderByStartDesc(userId,
                        Status.WAITING, pageable));
            default:
                throw new StateException();
        }
    }

    void validateDateTimeOfBooking(BookingInDto bookingInDto) {
        LocalDateTime startTime = bookingInDto.getStart();
        LocalDateTime endTime = bookingInDto.getEnd();

        if (startTime.isBefore(LocalDateTime.now()) || endTime.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Некорректное время бронирования");
        }
    }
}