package ru.rassafel.foodsharing.analyzer.exception;

import lombok.NoArgsConstructor;
import ru.rassafel.foodsharing.common.exception.ApiException;

/**
 * @author rassafel
 */
@NoArgsConstructor
public class ParseException extends ApiException {
    public ParseException(String message) {
        super(message);
    }
}
