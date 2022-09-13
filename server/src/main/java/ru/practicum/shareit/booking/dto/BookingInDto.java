package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Dto бронирования
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class BookingInDto {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private String status;
    private long bookerId;
    private long itemId;
    private String itemName;
}
