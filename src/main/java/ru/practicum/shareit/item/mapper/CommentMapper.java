package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

/**
 * Маппер между объектами Comment и CommentDto
 */
@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toComment(CommentDto dto);

    @Mapping(target = "authorName", source = "user.name")
    CommentDto toDto(Comment comment);

    @Mapping(target = "authorName", source = "user.name")
    List<CommentDto> toDto(Iterable<Comment> Comment);
}