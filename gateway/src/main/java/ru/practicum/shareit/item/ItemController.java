package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemShortDto;

import javax.validation.Valid;

/**
 * Основной контроллер для работы с вещами
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    /**
     * Возвращает список всех вещей пользователя userId
     *
     * @param userId объекта пользователя
     * @return списка объектов вещей
     */
    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        return itemClient.getItems(userId, from, size);
    }

    /**
     * Возвращает вещь по itemId независимо от userId пользователя
     *
     * @param itemId объекта вещи
     * @return объект вещи
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                               @PathVariable long itemId) {
        return itemClient.getItemById(userId, itemId);
    }

    /**
     * Возвращает список по введенному тексту поиска text
     *
     * @param text поиска
     * @return список объектов вещей
     */
    @GetMapping("/search")
    public ResponseEntity<Object> searchItemByText(@RequestParam String text,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size) {
        return itemClient.searchItemByText(text, from, size);
    }

    /**
     * Создаёт объект вещи пользователя userId
     *
     * @param userId объекта пользователя
     * @return возвращает объект вещи, который был создан
     */
    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader("X-Sharer-User-Id") long userId,
                            @Valid @RequestBody ItemShortDto itemShortDto) {
        return itemClient.addNewItem(userId, itemShortDto);
    }

    /**
     * Создаёт комментарий вещи пользователя userId после бронирования
     *
     * @param userId объекта пользователя
     * @return возвращает объект вещи, который был создан
     */
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @PathVariable long itemId,
                                 @Valid @RequestBody CommentDto commentDto) {
        return itemClient.addComment(userId, itemId, commentDto);
    }

    /**
     * Обновляет данные объекта вещи itemId пользователя userId
     *
     * @param itemId объекта вещи
     * @param userId объекта пользователя
     * @return возвращает обновленный объект вещи
     */
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") long userId,
                               @PathVariable long itemId,
                               @RequestBody ItemShortDto itemShortDto) {
        return itemClient.updateItem(userId, itemId, itemShortDto);
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
        itemClient.deleteItem(userId, itemId);
    }
}