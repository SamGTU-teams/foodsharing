package ru.rassafel.foodsharing.analyzer.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/**
 * @author rassafel
 */
@Data
@RequiredArgsConstructor
public class ScoreProductDto {
    @NotNull
    private final ProductDto product;
    @NotNull
    @PositiveOrZero
    private final Float score;
}
