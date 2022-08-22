package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import java.util.List;

/**
 * Интерфейс для бронирования вещей
 */
public interface BookingService {
    List<BookingOutDto> getBookings(long userId);

    BookingOutDto getBookingById(long userId, long bookingId);

    List<BookingOutDto> getBookingsByBookerId(long userId, State state);

    List<BookingOutDto> getBookingsByOwnerId(long userId, State state);

    BookingInDto addNewBooking(long userId, BookingInDto bookingInDto);

    BookingOutDto updateBookingStatus(long userId, long bookingId, boolean approved);

    void deleteBooking(long userId, long bookingId);
}