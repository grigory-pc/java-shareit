package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

/**
 * Основной контроллер для работы с бронированием
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    /**
     * Возвращает информацию о брони по bookingId для определенного userId пользователя или владельца вещи
     *
     * @param bookingId объекта вещи
     * @return объект вещи
     */
    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @PathVariable long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    /**
     * Возвращает список всех бронирований для определенного userId пользователя
     *
     * @param state объекта вещи
     * @return объект вещи
     */
    @GetMapping()
    public List<BookingDto> getBookingsByBookerId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.getBookingsByBookerId(userId, state);
    }

    /**
     * Возвращает список всех бронирований для определенного userId пользователя - владельца вещи
     *
     * @param state объекта вещи
     * @return объект вещи
     */
    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwnerId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.getBookingsByOwnerId(userId, state);
    }

    /**
     * Создаёт объект бронирования
     *
     * @param userId объекта пользователя
     * @return возвращает объект бронирования, который был создан
     */
    @PostMapping
    public BookingDto add(@RequestHeader("X-Sharer-User-Id") long userId,
                          @Valid @RequestBody BookingDto bookingDto) {
        return bookingService.addNewBooking(userId, bookingDto);
    }

    /**
     * Обновляет данные объекта бронирования bookingId пользователя userId
     *
     * @param bookingId объекта бронирования
     * @param userId    объекта пользователя
     * @param approved  - true или false
     * @return возвращает обновленный объект бронирования
     */
    @PatchMapping("/{bookingId}?approved={approved}")
    public BookingDto update(@RequestHeader("X-Sharer-User-Id") long userId,
                             @PathVariable long bookingId,
                             @PathVariable boolean approved,
                             @RequestBody BookingDto bookingDto) {
        return bookingService.updateBooking(userId, bookingId, approved, bookingDto);
    }


}
