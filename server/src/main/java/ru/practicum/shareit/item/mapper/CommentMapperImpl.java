package ru.practicum.shareit.item.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDto.CommentDtoBuilder;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Comment.CommentBuilder;
import ru.practicum.shareit.user.model.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-09-08T19:18:17+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.13 (Amazon.com Inc.)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public Comment toComment(CommentDto dto) {
        if ( dto == null ) {
            return null;
        }

        CommentBuilder comment = Comment.builder();

        comment.id( dto.getId() );
        comment.text( dto.getText() );
        comment.created( dto.getCreated() );

        return comment.build();
    }

    @Override
    public CommentDto toDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentDtoBuilder commentDto = CommentDto.builder();

        commentDto.authorName( commentUserName( comment ) );
        commentDto.id( comment.getId() );
        commentDto.text( comment.getText() );
        commentDto.created( comment.getCreated() );

        return commentDto.build();
    }

    @Override
    public List<CommentDto> toDto(Iterable<Comment> Comment) {
        if ( Comment == null ) {
            return null;
        }

        List<CommentDto> list = new ArrayList<CommentDto>();
        for ( Comment comment : Comment ) {
            list.add( toDto( comment ) );
        }

        return list;
    }

    private String commentUserName(Comment comment) {
        if ( comment == null ) {
            return null;
        }
        User user = comment.getUser();
        if ( user == null ) {
            return null;
        }
        String name = user.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
