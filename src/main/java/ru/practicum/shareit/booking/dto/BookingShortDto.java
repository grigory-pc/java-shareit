package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Dto бронирования
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BookingShortDto {
    private long id;
    private long bookerId;
}
