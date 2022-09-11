package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * Dto комментария
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CommentDto {
    private long id;
    @NotBlank
    private String text;
    private String authorName;
    private LocalDate created;
}