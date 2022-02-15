package ru.rassafel.foodsharing.ibot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.rassafel.foodsharing.common.model.entity.Product;
import ru.rassafel.foodsharing.ibot.model.entity.FoodPost;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rassafel
 */
public interface FoodPostRepository extends JpaRepository<FoodPost, Long> {
//    https://postgis.net/docs/manual-1.4/ST_Distance_Sphere.html
//    https://houarinourreddine.medium.com/integrate-spring-boot-and-postgis-to-manage-spatial-data-272edacf2cb
//    https://github.com/The-Code-Mastery/gis-rest-api-with-java

    // ToDo: Create native query using postgis
    @Query(value = "", nativeQuery = true)
    List<FoodPost> findNearbyPosts(double lat, double lon, int range,
                                   LocalDateTime after,
                                   List<Product> products);
}
