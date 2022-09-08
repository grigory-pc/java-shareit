package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

/**
 * Основной контроллер для работы с запросами
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    /**
     * Возвращает список всех запросов для определенного userId пользователя
     *
     * @return список запросов пользователя
     */
    @GetMapping()
    public List<ItemRequestDto> getItemRequestsByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getItemRequestByUserId(userId);
    }

    /**
     * Возвращает список запросов, созданных другими пользователями
     *
     * @param userId объекта пользователя
     * @return список запросов, созданных другими пользователями
     */
    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                   @RequestParam(defaultValue = "0") int from,
                                                   @RequestParam(defaultValue = "10") int size) {
        return itemRequestService.getAllItemRequest(userId, from, size);
    }

    /**
     * Возвращает информацию о запросе по requestId для любого пользователя
     *
     * @param requestId объекта запроса
     * @return объект запроса
     */
    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long requestId) {
        return itemRequestService.getItemRequestById(userId, requestId);
    }

    /**
     * Создаёт объект запроса
     *
     * @param userId объекта пользователя
     * @return возвращает объект запроса, который был создан
     */
    @PostMapping
    public ItemRequestDto addItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.addItemRequest(userId, itemRequestDto);
    }
}