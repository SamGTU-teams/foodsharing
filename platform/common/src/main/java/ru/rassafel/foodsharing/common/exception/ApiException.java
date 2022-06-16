package ru.rassafel.foodsharing.common.exception;

import lombok.RequiredArgsConstructor;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
