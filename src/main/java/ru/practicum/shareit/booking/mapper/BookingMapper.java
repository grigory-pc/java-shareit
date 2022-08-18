package ru.practicum.shareit.booking.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

/**
 * Маппер между объектами Booking и BookingDto
 */
@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking toBooking(BookingDto dto);

    @Mapping(target="bookerId", source="user.id")
    @Mapping(target="itemId", source="item.id")
    @Mapping(target="itemName", source="item.name")
    BookingDto toDto(Booking booking);

    @Mapping(target="bookerId", source="user.id")
    @Mapping(target="itemId", source="item.id")
    @Mapping(target="itemName", source="item.name")
    List<BookingDto> toDto(Iterable<Booking> booking);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBookingFromDto(BookingDto bookingDto, @MappingTarget Booking booking);
}