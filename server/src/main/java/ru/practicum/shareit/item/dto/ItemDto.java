package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Dto вещи
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemDto {
    private long id;
    private String name;
    private String description;
    @JsonSerialize(using = StringBooleanSerializer.class)
    private String available;
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;
    private List<CommentDto> comments;
    private int requestId;
}