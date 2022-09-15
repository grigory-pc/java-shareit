package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Dto вещи
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemShortDto {
    private long id;
    private String name;
    private String description;
    @JsonSerialize(using = StringBooleanSerializer.class)
    private String available;
    private int requestId;
}