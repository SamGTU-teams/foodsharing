package ru.rassafel.foodsharing.ibot.model.dto;

import lombok.Data;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rassafel
 */
@Data
public class RequestFoodPostDto {
    @NotEmpty
    private List<@NotNull ProductDto> products;

    @NotNull
    private GeoPoint point;

    private Integer range = 1;

    private LocalDateTime after = LocalDateTime.now().minusHours(4);
}
