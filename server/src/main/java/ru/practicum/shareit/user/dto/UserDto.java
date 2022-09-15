package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Dto пользователя
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDto {
    private long id;
    private String email;
    private String name;
}