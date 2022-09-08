package ru.practicum.shareit;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;

@Service
public class Validation {
    public void validationId(long id) {
        if (id <= 0) {
            throw new ValidationException("id должен быть больше 0");
        }
    }

    public void validationFrom(long from) {
        if (from< 0) {
            throw new ValidationException("from должен быть не меньше 0");
        }
    }
}