package ru.practicum.shareit.item.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDto.ItemDtoBuilder;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.dto.ItemShortDto.ItemShortDtoBuilder;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.Item.ItemBuilder;
import ru.practicum.shareit.requests.model.ItemRequest;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-09-08T19:18:17+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.13 (Amazon.com Inc.)"
)
@Component
public class ItemMapperImpl implements ItemMapper {

    @Override
    public Item toItem(ItemShortDto dto) {
        if ( dto == null ) {
            return null;
        }

        ItemBuilder item = Item.builder();

        item.id( dto.getId() );
        item.name( dto.getName() );
        item.description( dto.getDescription() );
        item.available( dto.getAvailable() );

        return item.build();
    }

    @Override
    public ItemShortDto toShortDto(Item item) {
        if ( item == null ) {
            return null;
        }

        ItemShortDtoBuilder itemShortDto = ItemShortDto.builder();

        itemShortDto.requestId( (int) itemItemRequestId( item ) );
        itemShortDto.id( item.getId() );
        itemShortDto.name( item.getName() );
        itemShortDto.description( item.getDescription() );
        itemShortDto.available( item.getAvailable() );

        return itemShortDto.build();
    }

    @Override
    public ItemDto toDto(Item item) {
        if ( item == null ) {
            return null;
        }

        ItemDtoBuilder itemDto = ItemDto.builder();

        itemDto.requestId( (int) itemItemRequestId( item ) );
        itemDto.id( item.getId() );
        itemDto.name( item.getName() );
        itemDto.description( item.getDescription() );
        itemDto.available( item.getAvailable() );

        return itemDto.build();
    }

    @Override
    public List<ItemShortDto> toShortDto(Iterable<Item> item) {
        if ( item == null ) {
            return null;
        }

        List<ItemShortDto> list = new ArrayList<ItemShortDto>();
        for ( Item item1 : item ) {
            list.add( toShortDto( item1 ) );
        }

        return list;
    }

    @Override
    public List<ItemDto> toDto(Iterable<Item> item) {
        if ( item == null ) {
            return null;
        }

        List<ItemDto> list = new ArrayList<ItemDto>();
        for ( Item item1 : item ) {
            list.add( toDto( item1 ) );
        }

        return list;
    }

    @Override
    public void updateItemFromDto(ItemShortDto itemShortDto, Item item) {
        if ( itemShortDto == null ) {
            return;
        }

        item.setId( itemShortDto.getId() );
        if ( itemShortDto.getName() != null ) {
            item.setName( itemShortDto.getName() );
        }
        if ( itemShortDto.getDescription() != null ) {
            item.setDescription( itemShortDto.getDescription() );
        }
        if ( itemShortDto.getAvailable() != null ) {
            item.setAvailable( itemShortDto.getAvailable() );
        }
    }

    private long itemItemRequestId(Item item) {
        if ( item == null ) {
            return 0L;
        }
        ItemRequest itemRequest = item.getItemRequest();
        if ( itemRequest == null ) {
            return 0L;
        }
        long id = itemRequest.getId();
        return id;
    }
}
