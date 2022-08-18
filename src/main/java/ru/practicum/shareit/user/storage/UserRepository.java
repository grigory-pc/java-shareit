package ru.practicum.shareit.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.user.model.User;

/**
 * Интерфейс для хранения объектов пользователей
 */
public interface UserRepository extends JpaRepository<User, Long>, CrudRepository<User, Long> {
    User findById(long id);
}