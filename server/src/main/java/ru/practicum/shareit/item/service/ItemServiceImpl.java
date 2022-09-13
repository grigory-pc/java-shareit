package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.OffsetBasedPageRequest;
import ru.practicum.shareit.Validation;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, ответственный за операции с вещами
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final Validation validation;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    /**
     * Возвращает список всех вещей пользователя
     */
    @Override
    public List<ItemDto> getItems(long userId, int from, int size) {
        Pageable pageable = OffsetBasedPageRequest.of(from, size);

        List<ItemDto> itemsDto = itemMapper.toDto(itemRepository.findAllByUserId(userId, pageable));

        for (ItemDto existItemDto : itemsDto) {
            setBookings(existItemDto, userId, existItemDto.getId());
        }
        return itemsDto;
    }

    /**
     * Возвращает вещь по ID (ItemDto)
     */
    @Override
    public ItemDto getItemDtoById(long userId, long itemId) {
        if (itemRepository.findById(itemId) == null) {
            throw new NotFoundException("Вещь не найдена");
        }
        ItemDto existItemDto = itemMapper.toDto(itemRepository.findById(itemId));
        List<CommentDto> existItemComments = commentMapper.toDto(commentRepository.findAllByItemId(itemId));

        existItemDto.setComments(existItemComments);

        return setBookings(existItemDto, userId, itemId);
    }

    /**
     * Возвращает вещь по ID (Item)
     */
    @Override
    public Item getItemById(long itemId) {
        if (itemRepository.findById(itemId) == null) {
            throw new NotFoundException("Вещь не найдена");
        }
        return itemRepository.findById(itemId);
    }

    /**
     * Добавляет вещь
     */
    @Override
    @Transactional
    public ItemShortDto addNewItem(long userId, ItemShortDto itemShortDto) {
        Item itemForSave = itemMapper.toItem(itemShortDto);

        itemForSave.setUser(getExistUser(userId));
        itemForSave.setItemRequest(itemRequestRepository.findById(itemShortDto.getRequestId()));

        return itemMapper.toShortDto(itemRepository.save(itemForSave));
    }

    @Override
    @Transactional
    public CommentDto addComment(long userId, long itemId, CommentDto commentDto) {
        Booking lastBooking = bookingRepository.findByUserIdAndItemIdAndEndIsBefore(userId, itemId,
                LocalDateTime.now());

        try {
            if (lastBooking.getUser().getId() != userId) {
                throw new ValidationException("Комментарий может оставлять только тот, кто бронировал вещь ранее");
            } else {
                Comment commentForSave = commentMapper.toComment(commentDto);

                commentForSave.setItem(itemRepository.findById(itemId));
                commentForSave.setUser(userRepository.findById(userId));
                commentForSave.setCreated(LocalDate.now());

                return commentMapper.toDto(commentRepository.save(commentForSave));
            }
        } catch (NullPointerException e) {
            throw new ValidationException(e + "Комментарий может оставлять только тот, кто бронировал вещь ранее");
        }
    }

    /**
     * Валидирует поля объекта и обновляет объект вещи
     */
    @Override
    @Transactional
    public ItemShortDto updateItem(long userId, long itemId, ItemShortDto itemShortDto) {
        userService.getUserById(userId);

        if (itemRepository.findAllByIdAndUserId(itemId, userId).size() == 0) {
            throw new NotFoundException(String.format("Вещь с id %d для данного пользователя не найдена", itemId));
        }

        itemShortDto.setId(itemId);

        Item itemForUpdate = itemRepository.findById(itemId);
        itemMapper.updateItemFromDto(itemShortDto, itemForUpdate);
        itemForUpdate.setUser(getExistUser(userId));
        Item updatedItem = itemRepository.save(itemForUpdate);

        return itemMapper.toShortDto(updatedItem);
    }

    /**
     * Удаление вещи
     */
    @Override
    @Transactional
    public void deleteItem(long userId, long itemId) {
        itemRepository.deleteById(itemId);
    }

    /**
     * Поиск вещи по тексту
     */
    @Override
    public List<ItemShortDto> searchItemByText(String text, int from, int size) {
        Pageable pageable = OffsetBasedPageRequest.of(from, size);

        if (text.isBlank()) {
            return itemMapper.toShortDto(new ArrayList<>());
        } else {
            return itemMapper.toShortDto(itemRepository.findAllByAvailableAndDescriptionContainingIgnoreCase("true",
                    text, pageable));
        }
    }

    private User getExistUser(long userId) {
        return userMapper.toUser(userService.getUserById(userId));
    }

    private ItemDto setBookings(ItemDto itemDto, long userId, long itemId) {
        Booking lastBookings = bookingRepository.findByItemIdAndItemUserIdAndEndIsBeforeOrderByStart(itemId, userId,
                LocalDateTime.now());

        Booking nextBookings = bookingRepository.findByItemIdAndItemUserIdAndStartIsAfterOrderByStart(itemId, userId,
                LocalDateTime.now());

        itemDto.setLastBooking(bookingMapper.toShortDto(lastBookings));
        itemDto.setNextBooking(bookingMapper.toShortDto(nextBookings));

        return itemDto;
    }
}