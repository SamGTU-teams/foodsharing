package ru.rassafel.foodsharing.common.exception;

import lombok.NoArgsConstructor;

/**
 * @author rassafel
 */
@NoArgsConstructor
public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
