package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * Dto бронирования
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BookingDto {
    private long id;
//    @NotBlank
    private LocalDateTime start;
//    @NotBlank
    private LocalDateTime end;
//    @NotBlank
    private String status;
//    @NotBlank
    private long bookerId;
//    @NotBlank
    private long itemId;
//    @NotBlank
    private String itemName;
}
