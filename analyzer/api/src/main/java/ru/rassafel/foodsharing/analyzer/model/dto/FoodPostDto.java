package ru.rassafel.foodsharing.analyzer.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;
import ru.rassafel.foodsharing.common.model.dto.RegionDto;

import javax.validation.constraints.NotBlank;
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
public class FoodPostDto {
    private String url;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime date;

    @NotBlank
    private String text;

    private List<String> attachments = new ArrayList<>();

    @NotNull
    private GeoPoint point;

    private List<RegionDto> regions = new ArrayList<>();

    @NotEmpty
    private List<ProductDto> products = new ArrayList<>();
}
