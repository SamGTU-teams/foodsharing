package ru.rassafel.foodsharing.ibot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rassafel.foodsharing.ibot.model.entity.FoodPost;

/**
 * @author rassafel
 */
public interface FoodPostRepository extends JpaRepository<FoodPost, Long> {
//    https://postgis.net/docs/manual-1.4/ST_Distance_Sphere.html
//    https://houarinourreddine.medium.com/integrate-spring-boot-and-postgis-to-manage-spatial-data-272edacf2cb
//    https://github.com/The-Code-Mastery/gis-rest-api-with-java
}
