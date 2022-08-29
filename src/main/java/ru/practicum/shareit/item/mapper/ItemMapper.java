package ru.practicum.shareit.item.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Маппер между объектами Item и ItemDto
 */
@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item toItem(ItemShortDto dto);

    @Mapping(target = "requestId", source = "itemRequest.id")
    ItemShortDto toShortDto(Item item);

    @Mapping(target = "requestId", source = "itemRequest.id")
    ItemDto toDto(Item item);

    List<ItemShortDto> toShortDto(Iterable<Item> item);

    @Mapping(target = "requestId", source = "itemRequest.id")
    List<ItemDto> toDto(Iterable<Item> item);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateItemFromDto(ItemShortDto itemShortDto, @MappingTarget Item item);
}