package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Dto бронирования
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BookingInDto {
    private long id;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    private String status;
    private long bookerId;
    @NotNull
    private long itemId;
    private String itemName;
}
