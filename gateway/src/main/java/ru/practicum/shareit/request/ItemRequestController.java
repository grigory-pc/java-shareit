package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * Основной контроллер для работы с запросами
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    /**
     * Возвращает список всех запросов для определенного userId пользователя
     *
     * @return список запросов пользователя
     */
    @GetMapping()
    public ResponseEntity<Object> getItemRequestByUserId(@Positive @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestClient.getItemRequestByUserId(userId);
    }

    /**
     * Возвращает список запросов, созданных другими пользователями
     *
     * @param userId объекта пользователя
     * @return список запросов, созданных другими пользователями
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                                     @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                     @Positive @RequestParam(defaultValue = "10") int size) {
        return itemRequestClient.getAllItemRequests(userId, from, size);
    }

    /**
     * Возвращает информацию о запросе по requestId для любого пользователя
     *
     * @param requestId объекта запроса
     * @return объект запроса
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                                     @Positive @PathVariable long requestId) {
        return itemRequestClient.getItemRequestById(userId, requestId);
    }

    /**
     * Создаёт объект запроса
     *
     * @param userId объекта пользователя
     * @return возвращает объект запроса, который был создан
     */
    @PostMapping
    public ResponseEntity<Object> addItemRequest(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                                 @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestClient.addItemRequest(userId, itemRequestDto);
    }
}