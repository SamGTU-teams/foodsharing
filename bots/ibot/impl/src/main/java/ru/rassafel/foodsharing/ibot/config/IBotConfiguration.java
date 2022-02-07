package ru.rassafel.foodsharing.ibot.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import ru.rassafel.foodsharing.ibot.model.entity.FoodPost;

/**
 * @author rassafel
 */
@Configuration
@EntityScan(basePackageClasses = FoodPost.class)
public class IBotConfiguration {
}
