package ru.rassafel.foodsharing.ibot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rassafel
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RequestFoodPostDto {
    @NotEmpty
    private List<@NotNull ProductDto> products = new ArrayList<>();

    @NotNull
    private GeoPoint point;

    private Integer range = 1;

    private LocalDateTime after = LocalDateTime.now().minusHours(4);
}
