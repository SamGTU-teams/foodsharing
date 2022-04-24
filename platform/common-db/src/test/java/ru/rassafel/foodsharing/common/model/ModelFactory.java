package ru.rassafel.foodsharing.common.model;

import ru.rassafel.foodsharing.common.model.dto.CategoryDto;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;
import ru.rassafel.foodsharing.common.model.dto.RegionDto;
import ru.rassafel.foodsharing.common.model.entity.geo.GeoPointEmbeddable;
import ru.rassafel.foodsharing.common.model.entity.product.Category;
import ru.rassafel.foodsharing.common.model.entity.product.Product;
import ru.rassafel.foodsharing.common.model.entity.geo.Region;

import java.util.Set;

/**
 * @author rassafel
 */
public class ModelFactory {

    public static Long entityId = 1L;
    public static Long subEntityId = 2L;

    public static String entityName = "Entity name";
    public static String subEntityName = "SubEntity name";

    public static CategoryDto createCategoryDto(Long id, String name) {
        CategoryDto result = new CategoryDto();
        result.setId(id);
        result.setName(name);
        return result;
    }

    public static Category createCategory(Long id, String name, Long productId, String productName) {
        Category result = createCategory(id, name);
        Product product = createProduct(productId, productName);
        product.setCategory(result);
        result.setProducts(Set.of(product));
        return result;
    }

    public static Category createCategory(Long id, String name) {
        Category result = new Category();
        result.setId(id);
        result.setName(name);
        return result;
    }

    public static Product createProduct(Long id, String name) {
        Product result = new Product();
        result.setId(id);
        result.setName(name);
        return result;
    }

    public static ProductDto createProductDto(Long id, String name) {
        ProductDto result = new ProductDto();
        result.setId(id);
        result.setName(name);
        return result;
    }

    public static GeoPoint createPointDto(double lat, double lon) {
        GeoPoint result = new GeoPoint();
        result.setLat(lat);
        result.setLon(lon);
        return result;
    }

    public static GeoPointEmbeddable createPoint(double lat, double lon) {
        GeoPointEmbeddable result = new GeoPointEmbeddable();
        result.setLat(lat);
        result.setLon(lon);
        return result;
    }

    public static Region createRegion(Long id, String name) {
        Region result = new Region();
        result.setId(id);
        result.setName(name);
        return result;
    }

    public static Region createRegion(Long id, String name, double lat, double lon) {
        Region result = createRegion(id, name);
        GeoPointEmbeddable point = createPoint(lat, lon);
        result.setPoint(point);
        return result;
    }

    public static RegionDto createRegionDto(Long id, String name) {
        RegionDto result = new RegionDto();
        result.setId(id);
        result.setName(name);
        return result;
    }

    public static RegionDto createRegionDto(Long id, String name, double lat, double lon) {
        RegionDto result = createRegionDto(id, name);
        GeoPoint point = createPointDto(lat, lon);
        result.setPoint(point);
        return result;
    }
}
