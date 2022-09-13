package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

/**
 * Основной контроллер для работы с пользователями
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    /**
     * Возвращает список объектов всех пользователей
     */
    @GetMapping()
    public ResponseEntity<Object> findAll() {
        return userClient.getUserAll();
    }

    /**
     * Возвращает объект пользователя по ID
     *
     * @param id объекта пользователя
     * @return объект пользователя
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@Positive @PathVariable long id) {
        return userClient.getUserById(id);
    }

    /**
     * Создаёт объект пользователя
     *
     * @return возвращает объект пользователя, который был создан
     */
    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto userDto) {
        return userClient.addNewUser(userDto);
    }

    /**
     * Обновляет данные пользователя
     *
     * @param userId пользователя
     * @return возвращает обновленный объект пользователя
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@Positive @PathVariable long userId,
                                         @RequestBody UserDto userDto) {
        return userClient.updateUser(userId, userDto);
    }

    /**
     * Удаляет объект пользователя
     *
     * @param id пользователя, которого удалют
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@Positive @PathVariable long id) {
        userClient.deleteUser(id);
    }
}