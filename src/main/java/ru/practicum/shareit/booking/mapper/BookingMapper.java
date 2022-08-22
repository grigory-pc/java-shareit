package ru.practicum.shareit.booking.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

/**
 * Маппер между объектами Booking и BookingDto
 */
@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking toBooking(BookingInDto dto);

    BookingInDto toDto(Booking booking);

    List<BookingInDto> toDto(Iterable<Booking> booking);

    @Mapping(target = "booker", source = "user")
    BookingOutDto toOutDto(Booking booking);

    @Mapping(target = "booker", source = "user")
    List<BookingOutDto> toOutDto(Iterable<Booking> booking);

    @Mapping(target = "bookerId", source = "user.id")
    BookingShortDto toShortDto(Booking booking);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBookingFromDto(BookingInDto bookingInDto, @MappingTarget Booking booking);
}