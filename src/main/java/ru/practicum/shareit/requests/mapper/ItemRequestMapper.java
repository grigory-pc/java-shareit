package ru.practicum.shareit.requests.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

/**
 * Маппер между объектами ItemRequest и ItemRequestDto
 */
@Mapper(componentModel = "spring")
public interface ItemRequestMapper {
    ItemRequest toItemRequest(ItemRequestDto dto);

    ItemRequestDto toDto(ItemRequest itemRequest);

    List<ItemRequestDto> toDto(Iterable<ItemRequest> itemRequest);
}
