package ru.practicum.shareit.item.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Интерфейс для хранения объектов вещей
 */
public interface ItemRepository extends JpaRepository<Item, Long>, CrudRepository<Item, Long> {
    Item findById(long id);

    List<Item> findAllByUserId(long userId, Pageable pageable);

    List<Item> findAllByIdAndUserId(long id, long userId);

    List<Item> findAllByAvailableAndDescriptionContainingIgnoreCase(String available, String text, Pageable pageable);

    List<Item> findAllByItemRequestId(long requestId);
}