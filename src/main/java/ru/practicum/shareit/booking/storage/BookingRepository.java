package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;


/**
 * Интерфейс для хранения объектов бронирования
 */
public interface BookingRepository extends JpaRepository<Booking, Long>, CrudRepository<Booking, Long> {
    Booking findById(long id);

    List<Booking> findAllByUserId(long userId);
    Booking findAllByItemId(long itemId);
}
