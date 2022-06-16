package ru.rassafel.foodsharing.analyzer.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/**
 * @author rassafel
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ScoreProductDto {
    @NotNull
    private ProductDto product;
    @NotNull
    @PositiveOrZero
    private Float score;
}
