package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemShortDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс, ответственный за хранение объектов вещей в памяти
 */
@Repository
public class ItemStorageInMemory implements ItemStorage {
    private long itemIdGenerated;
    private final Map<Long, List<ItemShortDto>> userItems = new HashMap<>();

    /**
     * Возвращает список всех вещей пользователя
     */
    @Override
    public List<ItemShortDto> getItems(long userId) {
        return userItems.get(userId);
    }

    /**
     * Возвращает вещь по ID
     */
    @Override
    public ItemShortDto getItemById(long itemId) {
        return userItems.values().stream()
                .flatMap(list -> list.stream())
                .filter(item -> item.getId() == itemId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id %d не найдена",
                        itemId)));
    }

    /**
     * Добавляет вещь
     */
    @Override
    public ItemShortDto add(long userId, ItemShortDto itemShortDto) {
        if (!userItems.containsKey(userId)) {
            userItems.put(userId, new ArrayList<>());
        }
        itemShortDto.setId(generateId());
        userItems.get(userId).add(itemShortDto);

        return itemShortDto;
    }

    /**
     * Валидирует поля объекта и обновляет вещь в памяти
     */
    @Override
    public ItemShortDto update(long userId, ItemShortDto itemShortDtoExisting, ItemShortDto itemShortDto) {
        if (!(itemShortDto.getAvailable() == null)) {
            itemShortDtoExisting.setAvailable(itemShortDto.getAvailable());
        }
        if (!(itemShortDto.getName() == null)) {
            itemShortDtoExisting.setName(itemShortDto.getName());
        }
        if (!(itemShortDto.getDescription() == null)) {
            itemShortDtoExisting.setDescription(itemShortDto.getDescription());
        }
        return itemShortDtoExisting;
    }

    /**
     * Удаление вещи из памяти
     */
    @Override
    public void delete(long userId, long itemId) {
        for (ItemShortDto itemOfUser : userItems.get(userId)) {
            if (itemOfUser.getId() == itemId) {
                userItems.get(userId).remove(itemOfUser);
            }
        }
    }

    @Override
    public List<ItemShortDto> search(String text) {
        List<ItemShortDto> foundItems = new ArrayList<>();

        if (!text.isBlank()) {
            foundItems = userItems.values().stream()
                    .flatMap(list -> list.stream())
                    .filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase())
                            && item.getAvailable().equals("true"))
                    .collect(Collectors.toList());
        }
        return foundItems;
    }

    /**
     * Возвращает вещь пользователя по ID
     */
    @Override
    public ItemShortDto getItemByIdAndUserId(long userId, long itemId) {
        if (!userItems.containsKey(userId)) {
            throw new NotFoundException(String.format("У пользователя с id %d нет вещей",
                    userId));
        }

        return userItems.get(userId).stream()
                .filter(p -> p.getId() == (itemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id %d не найдена",
                        itemId)));
    }

    private long generateId() {
        return ++itemIdGenerated;
    }
}