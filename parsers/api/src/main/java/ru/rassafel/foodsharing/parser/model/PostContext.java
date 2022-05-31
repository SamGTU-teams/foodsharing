package ru.rassafel.foodsharing.parser.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.dto.RegionDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

/**
 * @author rassafel
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class PostContext {
    @NotNull
    private List<@NotBlank String> attachments;

    @Null
    private GeoPoint point;

    @NotNull
    private List<@NotNull RegionDto> regions;
}
