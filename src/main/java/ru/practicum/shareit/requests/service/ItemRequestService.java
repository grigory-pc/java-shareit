package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.util.List;

/**
 * Интерфейс для сервиса запросов
 */
public interface ItemRequestService {
    List<ItemRequestDto> getItemRequestByUserId(long userId);

    List<ItemRequestDto> getAllItemRequest(long userId, int from, int size);

    ItemRequestDto getItemRequestById(long userId, long requestId);

    ItemRequestDto addItemRequest(long userId, ItemRequestDto itemRequestDto);
}