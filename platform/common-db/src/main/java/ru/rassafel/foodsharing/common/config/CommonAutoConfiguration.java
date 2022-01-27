package ru.rassafel.foodsharing.common.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rassafel.foodsharing.common.model.entity.Category;
import ru.rassafel.foodsharing.common.model.mapper.CategoryMapper;
import ru.rassafel.foodsharing.common.model.mapper.GeoPointEmbeddableMapper;
import ru.rassafel.foodsharing.common.model.mapper.ProductMapper;
import ru.rassafel.foodsharing.common.model.mapper.RegionMapper;

/**
 * @author rassafel
 */
@Configuration
@EntityScan(basePackageClasses = Category.class)
public class CommonAutoConfiguration {
    @Bean
    CategoryMapper categoryMapper() {
        return CategoryMapper.INSTANCE;
    }

    @Bean
    GeoPointEmbeddableMapper geoPointEmbeddableMapper() {
        return GeoPointEmbeddableMapper.INSTANCE;
    }

    @Bean
    ProductMapper productMapper() {
        return ProductMapper.INSTANCE;
    }

    @Bean
    RegionMapper regionMapper() {
        return RegionMapper.INSTANCE;
    }
}
