package ru.rassafel.foodsharing.analyzer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.rassafel.foodsharing.common.model.entity.product.Product;

/**
 * @author rassafel
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ScoreProduct {
    private Float score;
    private Product product;
}
