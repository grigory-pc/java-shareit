package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Интерфейс для сервиса вещей
 */
public interface ItemService {
    List<ItemDto> getItems(long userId);

    ItemDto getItemDtoById(long userId, long itemId);

    Item getItemById(long itemId);

    ItemShortDto addNewItem(long userId, ItemShortDto itemShortDto);

    CommentDto addComment(long userId, long itemId, CommentDto commentDto);

    ItemShortDto updateItem(long userId, long itemId, ItemShortDto itemShortDto);

    void deleteItem(long userId, long itemId);

    List<ItemShortDto> searchItemByText(String text);
}