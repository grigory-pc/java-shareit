package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

/**
 * Интерфейс для бронирования вещей
 */
public interface BookingService {
    List<BookingDto> getBookings(long userId);

    BookingDto getBookingById(long userId, long bookingId);

    List<BookingDto> getBookingsByBookerId(long userId, State state);

    List<BookingDto> getBookingsByOwnerId(long userId, State state);

    BookingDto addNewBooking(long userId, BookingDto bookingDto);

    BookingDto updateBooking(long userId, long bookingId, boolean approved, BookingDto bookingDto);

    void deleteBooking(long userId, long bookingId);


}
