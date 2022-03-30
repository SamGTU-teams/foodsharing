package ru.rassafel.foodsharing.parser.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

/**
 * @author rassafel
 */
@Data
public class RawPost {
    @Null
    private String url;

    @NotNull
    private LocalDateTime date;

    @NotBlank
    private String text;

    @NotNull
    private PostContext context;
}
