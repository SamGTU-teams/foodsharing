package ru.rassafel.foodsharing.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author rassafel
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
