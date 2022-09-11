package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Pageable;
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

    List<Booking> findAllByUserId_OrderByStartDesc(long userId, Pageable pageable);

    List<Booking> findAllByUserIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(long userId, LocalDateTime localDateTimeNow,
                                                                               LocalDateTime localDateTimeNowNow,
                                                                               Pageable pageable);

    List<Booking> findAllByUserIdAndStartIsAfterOrderByStartDesc(long userId, LocalDateTime localDateTimeNow,
                                                                 Pageable pageable);

    List<Booking> findAllByUserIdAndEndIsBeforeOrderByStartDesc(long userId, LocalDateTime localDateTimeNow,
                                                                Pageable pageable);

    List<Booking> findAllByUserIdAndStatusOrderByStartDesc(long userId, Status status, Pageable pageable);

    List<Booking> findAllByItemUserId_OrderByStartDesc(long itemId, Pageable pageable);

    List<Booking> findAllByItemUserIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(long userId, LocalDateTime localDateTimeNow,
                                                                                   LocalDateTime localDateTimeNowNow,
                                                                                   Pageable pageable);

    List<Booking> findAllByItemUserIdAndStartIsAfterOrderByStartDesc(long userId, LocalDateTime localDateTimeNow,
                                                                     Pageable pageable);

    List<Booking> findAllByItemUserIdAndEndIsBeforeOrderByStartDesc(long userId, LocalDateTime localDateTimeNow,
                                                                    Pageable pageable);

    List<Booking> findAllByItemUserIdAndStatusOrderByStartDesc(long userId, Status status,
                                                               Pageable pageable);

    Booking findByItemIdAndItemUserIdAndStartIsAfterOrderByStart(long itemId, long userId, LocalDateTime localDateTimeNow);

    Booking findByItemIdAndItemUserIdAndEndIsBeforeOrderByStart(long itemId, long userId, LocalDateTime localDateTimeNow);

    Booking findByUserIdAndItemIdAndEndIsBefore(long userId, long itemId, LocalDateTime localDateTime);
}