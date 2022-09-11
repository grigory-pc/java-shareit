package ru.practicum.shareit.requests.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto.ItemRequestDtoBuilder;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.model.ItemRequest.ItemRequestBuilder;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-09-08T19:18:17+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.13 (Amazon.com Inc.)"
)
@Component
public class ItemRequestMapperImpl implements ItemRequestMapper {

    @Override
    public ItemRequest toItemRequest(ItemRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        ItemRequestBuilder itemRequest = ItemRequest.builder();

        itemRequest.id( dto.getId() );
        itemRequest.description( dto.getDescription() );
        itemRequest.created( dto.getCreated() );

        return itemRequest.build();
    }

    @Override
    public ItemRequestDto toDto(ItemRequest itemRequest) {
        if ( itemRequest == null ) {
            return null;
        }

        ItemRequestDtoBuilder itemRequestDto = ItemRequestDto.builder();

        itemRequestDto.id( itemRequest.getId() );
        itemRequestDto.description( itemRequest.getDescription() );
        itemRequestDto.created( itemRequest.getCreated() );

        return itemRequestDto.build();
    }

    @Override
    public List<ItemRequestDto> toDto(Iterable<ItemRequest> itemRequest) {
        if ( itemRequest == null ) {
            return null;
        }

        List<ItemRequestDto> list = new ArrayList<ItemRequestDto>();
        for ( ItemRequest itemRequest1 : itemRequest ) {
            list.add( toDto( itemRequest1 ) );
        }

        return list;
    }
}
