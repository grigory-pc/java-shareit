package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Интерфейс для хранения объектов бронирования
 */
public interface BookingRepository extends JpaRepository<Booking, Long>, CrudRepository<Booking, Long> {
    Booking findById(long id);

    List<Booking> findAllByUserId_OrderByStartDesc(long userId);

    List<Booking> findAllByUserIdAndStartIsBeforeAndEndIsAfterOrderByStart(long userId, LocalDateTime localDateTimeNow,
                                                                           LocalDateTime localDateTimeNowNow);

    List<Booking> findAllByUserIdAndStartIsAfterOrderByStart(long userId, LocalDateTime localDateTimeNow);

    List<Booking> findAllByUserIdAndEndIsBeforeOrderByStart(long userId, LocalDateTime localDateTimeNow);

    List<Booking> findAllByUserIdAndStatusOrderByStart(long userId, Status status);

    List<Booking> findAllByItemUserId_OrderByStartDesc(long itemId);

    List<Booking> findAllByItemUserIdAndStartIsBeforeAndEndIsAfterOrderByStart(long userId, LocalDateTime localDateTimeNow,
                                                                               LocalDateTime localDateTimeNowNow);

    List<Booking> findAllByItemUserIdAndStartIsAfterOrderByStart(long userId, LocalDateTime localDateTimeNow);

    List<Booking> findAllByItemUserIdAndEndIsBeforeOrderByStart(long userId, LocalDateTime localDateTimeNow);

    List<Booking> findAllByItemUserIdAndStatusOrderByStart(long userId, Status status);

    Booking findByItemIdAndItemUserIdAndStartIsAfterOrderByStart(long itemId, long userId, LocalDateTime localDateTimeNow);

    Booking findByItemIdAndItemUserIdAndEndIsBeforeOrderByStart(long itemId, long userId, LocalDateTime localDateTimeNow);

    Booking findByUserIdAndItemIdAndEndIsBefore(long userId, long itemId, LocalDateTime localDateTime);
}