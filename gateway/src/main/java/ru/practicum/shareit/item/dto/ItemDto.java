package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingInDto;

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
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    @JsonSerialize(using = StringBooleanSerializer.class)
    private String available;
    private BookingInDto lastBooking;
    private BookingInDto nextBooking;
    private List<CommentDto> comments;
    private int requestId;
}