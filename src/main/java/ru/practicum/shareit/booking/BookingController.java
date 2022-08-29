package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

/**
 * Основной контроллер для работы с бронированием
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    /**
     * Возвращает информацию о брони по bookingId для определенного userId пользователя или владельца вещи
     *
     * @param bookingId объекта вещи
     * @return объект вещи
     */
    @GetMapping("/{bookingId}")
    public BookingOutDto getBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @PathVariable long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    /**
     * Возвращает список всех бронирований для определенного userId пользователя
     *
     * @param state объекта вещи
     * @return список объектов вещей
     */
    @GetMapping()
    public List<BookingOutDto> getBookingsByBookerId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                     @RequestParam(defaultValue = "ALL") State state,
                                                     @RequestParam(defaultValue = "0") long from,
                                                     @RequestParam(defaultValue = "10") int size) {
        return bookingService.getBookingsByBookerId(userId, state, from, size);
    }

    /**
     * Возвращает список всех бронирований для определенного userId пользователя - владельца вещи
     *
     * @param state объекта вещи
     * @return объект вещи
     */
    @GetMapping("/owner")
    public List<BookingOutDto> getBookingsByOwnerId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @RequestParam(defaultValue = "ALL") State state,
                                                    @RequestParam(defaultValue = "0") long from,
                                                    @RequestParam(defaultValue = "10") int size) {
        return bookingService.getBookingsByOwnerId(userId, state, from, size);
    }

    /**
     * Создаёт объект бронирования
     *
     * @param userId объекта пользователя
     * @return возвращает объект бронирования, который был создан
     */
    @PostMapping
    public BookingInDto addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @Valid @RequestBody BookingInDto bookingInDto) {
        return bookingService.addNewBooking(userId, bookingInDto);
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
    public BookingOutDto updateBookingStatus(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long bookingId,
                                             @RequestParam boolean approved) {
        return bookingService.updateBookingStatus(userId, bookingId, approved);
    }
}