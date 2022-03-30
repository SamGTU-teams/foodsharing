package ru.rassafel.foodsharing.analyzer.exception;

import lombok.NoArgsConstructor;

/**
 * @author rassafel
 */
@NoArgsConstructor
public class ProductParseException extends ParseException {
    public ProductParseException(String message) {
        super(message);
    }
}
