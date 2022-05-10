package ru.rassafel.foodsharing.parser.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.rassafel.foodsharing.parser.model.PostContext;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * @author rassafel
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class RawPostDto {
    //    Nullable and NotBlack
    @Pattern(regexp = "^(?!\\s*$).+")
    private String url;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime date;

    @NotBlank
    private String text;

    @NotNull
    private PostContext context;
}
