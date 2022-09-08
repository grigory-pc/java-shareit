package ru.practicum.shareit.requests.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.OffsetBasedPageRequest;
import ru.practicum.shareit.Validation;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.mapper.ItemRequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс, ответственный за операции с запросами
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final Validation validation;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemMapper itemMapper;
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;

    /**
     * Возвращает список запросов по ID пользователя
     */
    @Override
    public List<ItemRequestDto> getItemRequestByUserId(long userId) {
        validation.validationId(userId);

        userService.getUserById(userId);

        List<ItemRequest> existItemRequest = itemRequestRepository
                .findAllByUserIdOrderByCreatedDesc(userId);

        return setItems(existItemRequest);
    }

    /**
     * Возвращает список всех запросов
     */
    @Override
    public List<ItemRequestDto> getAllItemRequest(long userId, int from, int size) {
        validation.validationId(userId);

        Pageable pageable = OffsetBasedPageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "created"));

        List<ItemRequest> existItemRequest = itemRequestRepository.findAllByUserIdIsNot(userId, pageable);

        return setItems(existItemRequest);
    }

    /**
     * Возвращает запрос по ID
     */
    @Override
    public ItemRequestDto getItemRequestById(long userId, long requestId) {
        validation.validationId(userId);

        userService.getUserById(userId);

        if (itemRequestRepository.findById(requestId) == null) {
            throw new NotFoundException("Запрос не найден");
        }
        ItemRequestDto existItemRequestDto = itemRequestMapper.toDto(itemRequestRepository.findById(requestId));

        List<ItemDto> existItemDto = itemMapper.toDto(itemRepository.findAllByItemRequestId(requestId));

        existItemRequestDto.setItems(existItemDto);

        return existItemRequestDto;
    }

    /**
     * Добавляет запрос
     */
    @Override
    @Transactional
    public ItemRequestDto addItemRequest(long userId, ItemRequestDto itemRequestDto) {
        validation.validationId(userId);

        userService.getUserById(userId);

        ItemRequest itemRequestForSave = itemRequestMapper.toItemRequest(itemRequestDto);

        itemRequestForSave.setUser(userRepository.findById(userId));
        itemRequestForSave.setCreated(LocalDateTime.now());

        return itemRequestMapper.toDto(itemRequestRepository.save(itemRequestForSave));
    }

    private List<ItemRequestDto> setItems(List<ItemRequest> existItemRequest) {
        List<ItemRequestDto> existItemRequestDto = itemRequestMapper.toDto(existItemRequest);

        for (ItemRequestDto itemRequestDto : existItemRequestDto) {
            List<ItemDto> existItemDto = itemMapper.toDto(itemRepository.findAllByItemRequestId(itemRequestDto.getId()));

            itemRequestDto.setItems(existItemDto);
        }
        return existItemRequestDto;
    }
}