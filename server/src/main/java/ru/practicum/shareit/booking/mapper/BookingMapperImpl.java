package ru.practicum.shareit.booking.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingInDto.BookingInDtoBuilder;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.dto.BookingOutDto.BookingOutDtoBuilder;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Booking.BookingBuilder;
import ru.practicum.shareit.user.model.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-09-08T19:18:18+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.13 (Amazon.com Inc.)"
)
@Component
public class BookingMapperImpl implements BookingMapper {

    @Override
    public Booking toBooking(BookingInDto dto) {
        if ( dto == null ) {
            return null;
        }

        BookingBuilder booking = Booking.builder();

        booking.id( dto.getId() );
        booking.start( dto.getStart() );
        booking.end( dto.getEnd() );
        if ( dto.getStatus() != null ) {
            booking.status( Enum.valueOf( Status.class, dto.getStatus() ) );
        }

        return booking.build();
    }

    @Override
    public BookingInDto toDto(Booking booking) {
        if ( booking == null ) {
            return null;
        }

        BookingInDtoBuilder bookingInDto = BookingInDto.builder();

        bookingInDto.id( booking.getId() );
        bookingInDto.start( booking.getStart() );
        bookingInDto.end( booking.getEnd() );
        if ( booking.getStatus() != null ) {
            bookingInDto.status( booking.getStatus().name() );
        }

        return bookingInDto.build();
    }

    @Override
    public List<BookingInDto> toDto(Iterable<Booking> booking) {
        if ( booking == null ) {
            return null;
        }

        List<BookingInDto> list = new ArrayList<BookingInDto>();
        for ( Booking booking1 : booking ) {
            list.add( toDto( booking1 ) );
        }

        return list;
    }

    @Override
    public BookingOutDto toOutDto(Booking booking) {
        if ( booking == null ) {
            return null;
        }

        BookingOutDtoBuilder bookingOutDto = BookingOutDto.builder();

        bookingOutDto.booker( booking.getUser() );
        bookingOutDto.id( booking.getId() );
        bookingOutDto.start( booking.getStart() );
        bookingOutDto.end( booking.getEnd() );
        bookingOutDto.status( booking.getStatus() );
        bookingOutDto.item( booking.getItem() );

        return bookingOutDto.build();
    }

    @Override
    public List<BookingOutDto> toOutDto(Iterable<Booking> booking) {
        if ( booking == null ) {
            return null;
        }

        List<BookingOutDto> list = new ArrayList<BookingOutDto>();
        for ( Booking booking1 : booking ) {
            list.add( toOutDto( booking1 ) );
        }

        return list;
    }

    @Override
    public BookingShortDto toShortDto(Booking booking) {
        if ( booking == null ) {
            return null;
        }

        BookingShortDto bookingShortDto = new BookingShortDto();

        bookingShortDto.setBookerId( bookingUserId( booking ) );
        bookingShortDto.setId( booking.getId() );

        return bookingShortDto;
    }

    @Override
    public void updateBookingFromDto(BookingInDto bookingInDto, Booking booking) {
        if ( bookingInDto == null ) {
            return;
        }

        booking.setId( bookingInDto.getId() );
        if ( bookingInDto.getStart() != null ) {
            booking.setStart( bookingInDto.getStart() );
        }
        if ( bookingInDto.getEnd() != null ) {
            booking.setEnd( bookingInDto.getEnd() );
        }
        if ( bookingInDto.getStatus() != null ) {
            booking.setStatus( Enum.valueOf( Status.class, bookingInDto.getStatus() ) );
        }
    }

    private long bookingUserId(Booking booking) {
        if ( booking == null ) {
            return 0L;
        }
        User user = booking.getUser();
        if ( user == null ) {
            return 0L;
        }
        long id = user.getId();
        return id;
    }
}
