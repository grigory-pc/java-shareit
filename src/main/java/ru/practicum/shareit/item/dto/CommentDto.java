package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * Dto комментария
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CommentDto {
    private long id;
    @NotBlank
    private String text;
    String authorName;
    LocalDate created;
}