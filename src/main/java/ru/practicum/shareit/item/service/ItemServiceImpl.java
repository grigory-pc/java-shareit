package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Validation;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс, ответственный за операции с вещами
 */
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    @Autowired
    private Validation validation;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private UserMapper userMapper;


    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserService userService) {
        this.itemRepository = itemRepository;
        this.userService = userService;
    }

    /**
     * Возвращает список всех вещей пользователя
     */
    @Override
    public List<ItemDto> getItems(long userId) {
        List<Item> items = itemRepository.findAll().stream()
                .filter(item -> item.getUser().getId() == userId)
                .collect(Collectors.toList());

        return itemMapper.toDto(items);
    }

    /**
     * Возвращает вещь по ID
     */
    @Override
    public ItemDto getItemById(long itemId) {
        validation.validationId(itemId);

        if (itemRepository.findById(itemId) == null) {
            throw new NotFoundException("Вещь не найдена");
        }

        return itemMapper.toDto(itemRepository.findById(itemId));
    }

    /**
     * Добавляет вещь
     */
    @Override
    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        Item itemForSave = itemMapper.toItem(itemDto);
        itemForSave.setUser(getExistUser(userId));

        return itemMapper.toDto(itemRepository.save(itemForSave));
    }

    /**
     * Валидирует поля объекта и обновляет объект вещи
     */
    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        validation.validationId(itemId);
        validation.validationId(userId);

        userService.getUserById(userId);

        itemRepository.findAll().stream()
                .filter(item -> item.getUser().getId() == userId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id %d для данного пользователя не найдена",
                        itemId)));


        itemDto.setId(itemId);

        Item itemForUpdate = itemRepository.findById(itemId);
        itemMapper.updateItemFromDto(itemDto, itemForUpdate);
        itemForUpdate.setUser(getExistUser(userId));
        Item updatedItem = itemRepository.save(itemForUpdate);

        return itemMapper.toDto(updatedItem);
    }

    /**
     * Удаление вещи
     */
    @Override
    public void deleteItem(long userId, long itemId) {
        validation.validationId(userId);
        validation.validationId(itemId);

        itemRepository.deleteById(itemId);
    }

    /**
     * Поиск вещи по тексту
     */
    @Override
    public List<ItemDto> searchItemByText(String text) {
        List<Item> foundItems;

        if (text.isBlank()) {
            foundItems = new ArrayList<>();
        } else {
            foundItems = itemRepository.findAll().stream()
                    .filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase()))
                    .filter(item -> item.getAvailable().equals("true"))
                    .collect(Collectors.toList());
        }
        return itemMapper.toDto(foundItems);
    }

    private User getExistUser(long userId) {
        return userMapper.toUser(userService.getUserById(userId));
    }
}