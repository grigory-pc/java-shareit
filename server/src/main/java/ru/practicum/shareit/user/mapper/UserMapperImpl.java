package ru.practicum.shareit.user.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDto.UserDtoBuilder;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.User.UserBuilder;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-09-08T19:18:17+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.13 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserDto dto) {
        if ( dto == null ) {
            return null;
        }

        UserBuilder user = User.builder();

        user.id( dto.getId() );
        user.email( dto.getEmail() );
        user.name( dto.getName() );

        return user.build();
    }

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDtoBuilder userDto = UserDto.builder();

        userDto.id( user.getId() );
        userDto.email( user.getEmail() );
        userDto.name( user.getName() );

        return userDto.build();
    }

    @Override
    public List<UserDto> toDto(Iterable<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserDto> list = new ArrayList<UserDto>();
        for ( User user : users ) {
            list.add( toDto( user ) );
        }

        return list;
    }

    @Override
    public void updateUserFromDto(UserDto userDto, User user) {
        if ( userDto == null ) {
            return;
        }

        user.setId( userDto.getId() );
        if ( userDto.getEmail() != null ) {
            user.setEmail( userDto.getEmail() );
        }
        if ( userDto.getName() != null ) {
            user.setName( userDto.getName() );
        }
    }
}
