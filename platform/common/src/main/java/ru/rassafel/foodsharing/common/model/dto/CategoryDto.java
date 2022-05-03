package ru.rassafel.foodsharing.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author rassafel
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;

    private String name;
}
