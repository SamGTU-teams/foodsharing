package ru.rassafel.foodsharing.parser.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rassafel
 */
@Data
public class RawPostDto {
    private String url;

    private LocalDateTime date;

    private String text;

    private PostContext context;
}
