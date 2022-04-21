package ru.rassafel.foodsharing.analyzer.exception;

import lombok.NoArgsConstructor;

/**
 * @author rassafel
 */
@NoArgsConstructor
public class GeoPointParseException extends ParseException {
    public GeoPointParseException(String message) {
        super(message);
    }
}
