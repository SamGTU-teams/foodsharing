package ru.rassafel.foodsharing.parser.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author rassafel
 */
@Data
public class RawPost {
    @NotBlank
    private String url;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime date;

    @NotBlank
    private String text;

    @NotNull
    private PostContext context;
}
