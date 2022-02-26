package ru.rassafel.foodsharing.parser.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rassafel
 */
@Data
public class RawPost {
    private String url;

    private LocalDateTime date;

    private String text;

    private PostContext context;
}
