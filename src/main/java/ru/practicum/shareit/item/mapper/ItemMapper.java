package ru.practicum.shareit.item.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
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

    ItemShortDto toShortDto(Item item);

    ItemDto toDto(Item item);

    List<ItemShortDto> toShortDto(Iterable<Item> item);

    List<ItemDto> toDto(Iterable<Item> item);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateItemFromDto(ItemShortDto itemShortDto, @MappingTarget Item item);
}