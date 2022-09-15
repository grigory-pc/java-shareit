package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Dto запроса
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemRequestDto {
    private long id;
    @NotBlank
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items = new ArrayList<>();

    public LocalDateTime getCreated() {
        return created;
    }
}
