package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * Основной контроллер для работы с вещами
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    /**
     * Возвращает список всех вещей пользователя userId
     *
     * @param userId объекта пользователя
     * @return списка объектов вещей
     */
    @GetMapping
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") long userId,
                             @RequestParam(defaultValue = "0") int from,
                             @RequestParam(defaultValue = "10") int size) {
        return itemService.getItems(userId, from, size);
    }

    /**
     * Возвращает вещь по itemId независимо от userId пользователя
     *
     * @param itemId объекта вещи
     * @return объект вещи
     */
    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                               @PathVariable long itemId) {
        return itemService.getItemDtoById(userId, itemId);
    }

    /**
     * Возвращает список по введенному тексту поиска text
     *
     * @param text поиска
     * @return список объектов вещей
     */
    @GetMapping("/search")
    public List<ItemShortDto> searchItemByText(@RequestParam String text,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size) {
        return itemService.searchItemByText(text, from, size);
    }

    /**
     * Создаёт объект вещи пользователя userId
     *
     * @param userId объекта пользователя
     * @return возвращает объект вещи, который был создан
     */
    @PostMapping
    public ItemShortDto add(@RequestHeader("X-Sharer-User-Id") long userId,
                            @Valid @RequestBody ItemShortDto itemShortDto) {
        return itemService.addNewItem(userId, itemShortDto);
    }

    /**
     * Создаёт комментарий вещи пользователя userId после бронирования
     *
     * @param userId объекта пользователя
     * @return возвращает объект вещи, который был создан
     */
    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @PathVariable long itemId,
                                 @RequestBody CommentDto commentDto) {
        return itemService.addComment(userId, itemId, commentDto);
    }

    /**
     * Обновляет данные объекта вещи itemId пользователя userId
     *
     * @param itemId объекта вещи
     * @param userId объекта пользователя
     * @return возвращает обновленный объект вещи
     */
    @PatchMapping("/{itemId}")
    public ItemShortDto update(@RequestHeader("X-Sharer-User-Id") long userId,
                               @PathVariable long itemId,
                               @RequestBody ItemShortDto itemShortDto) {
        return itemService.updateItem(userId, itemId, itemShortDto);
    }

    /**
     * Удаляет объект вещи itemId пользователя userId
     *
     * @param itemId объекта вещи
     * @param userId объекта пользователя
     */
    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable long itemId) {
        itemService.deleteItem(userId, itemId);
    }
}