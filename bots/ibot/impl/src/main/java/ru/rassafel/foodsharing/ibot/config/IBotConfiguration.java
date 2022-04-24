package ru.rassafel.foodsharing.ibot.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rassafel.foodsharing.ibot.model.entity.FoodPost;
import ru.rassafel.foodsharing.ibot.model.mapper.FoodPostMapper;
import ru.rassafel.foodsharing.ibot.model.mapper.FoodPostMapperImpl;

/**
 * @author rassafel
 */
@Configuration
@EntityScan(basePackageClasses = FoodPost.class)
public class IBotConfiguration {

    @Bean
    public FoodPostMapper foodPostMapper(){
        return FoodPostMapper.INSTANCE;
    }

}
