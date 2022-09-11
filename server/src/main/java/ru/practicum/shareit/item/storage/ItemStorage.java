package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemShortDto;

import java.util.List;

/**
 * Интерфейс для хранения объектов вещей
 */
@Component
public interface ItemStorage {
    List<ItemShortDto> getItems(long userId);

    ItemShortDto getItemById(long id);

    ItemShortDto getItemByIdAndUserId(long userId, long itemId);

    ItemShortDto add(long userId, ItemShortDto itemShortDto);

    ItemShortDto update(long id, ItemShortDto itemShortDtoExisting, ItemShortDto itemShortDto);

    void delete(long userId, long itemId);

    List<ItemShortDto> search(String text);
}