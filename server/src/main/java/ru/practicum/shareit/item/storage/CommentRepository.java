package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

/**
 * Интерфейс для хранения объектов комментариев
 */
public interface CommentRepository extends JpaRepository<Comment, Long>, CrudRepository<Comment, Long> {
    List<Comment> findAllByItemId(long itemId);
}