package ru.rassafel.foodsharing.session.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class From {
    private String username;
    private String phone;
    private Long id;
}
