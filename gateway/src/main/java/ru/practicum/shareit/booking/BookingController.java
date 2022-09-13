package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.State;

import javax.validation.Valid;

/**
 * Основной контроллер для работы с бронированием
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingClient bookingClient;

    /**
     * Возвращает информацию о брони по bookingId для определенного userId пользователя или владельца вещи
     *
     * @param bookingId объекта вещи
     * @return объект вещи
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @PathVariable long bookingId) {
        return bookingClient.getBookingById(userId, bookingId);
    }

    /**
     * Возвращает список всех бронирований для определенного userId пользователя
     *
     * @param state объекта вещи
     * @return список объектов вещей
     */
    @GetMapping()
    public ResponseEntity<Object> getBookingsByBookerId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                        @RequestParam(defaultValue = "ALL") State state,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "10") int size) {
        return bookingClient.getBookingsByBookerId(userId, state, from, size);
    }

    /**
     * Возвращает список всех бронирований для определенного userId пользователя - владельца вещи
     *
     * @param state объекта вещи
     * @return объект вещи
     */
    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwnerId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                       @RequestParam(defaultValue = "ALL") State state,
                                                       @RequestParam(defaultValue = "0") int from,
                                                       @RequestParam(defaultValue = "10") int size) {
        return bookingClient.getBookingsByOwnerId(userId, state, from, size);
    }

    /**
     * Создаёт объект бронирования
     *
     * @param userId объекта пользователя
     * @return возвращает объект бронирования, который был создан
     */
    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @Valid @RequestBody BookingInDto bookingInDto) {
        return bookingClient.addNewBooking(userId, bookingInDto);
    }

    /**
     * Обновляет статус бронирования bookingId владельца вещи userId
     *
     * @param bookingId объекта бронирования
     * @param userId    объекта пользователя
     * @param approved  - true или false
     * @return возвращает обновленный объект бронирования
     */
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBookingStatus(@RequestHeader("X-Sharer-User-Id") long userId,
                                                      @PathVariable long bookingId,
                                                      @RequestParam boolean approved) {
        return bookingClient.updateBookingStatus(userId, bookingId, approved);
    }
}