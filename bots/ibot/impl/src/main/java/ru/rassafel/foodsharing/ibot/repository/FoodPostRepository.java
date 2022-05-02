package ru.rassafel.foodsharing.ibot.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.rassafel.foodsharing.ibot.model.entity.FoodPost;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author rassafel
 */
public interface FoodPostRepository extends JpaRepository<FoodPost, Long> {
//    https://postgis.net/docs/manual-1.4/ST_Distance_Sphere.html
//    https://houarinourreddine.medium.com/integrate-spring-boot-and-postgis-to-manage-spatial-data-272edacf2cb
//    https://github.com/The-Code-Mastery/gis-rest-api-with-java

    @Query(value = "SELECT DISTINCT fp.* FROM ibot.food_post fp " +
        "INNER JOIN ibot.food_post_products fpp ON fp.id = fpp.food_post_id " +
        "INNER JOIN public.product p ON fpp.product_id = p.id " +
        "WHERE " +
        "p.id IN :productIds " +
        "AND " +
        "fp.date >= :after " +
        "AND " +
        "ST_DWithin(CAST(ST_SetSRID(ST_MakePoint(fp.lon, fp.lat), 4326) AS GEOGRAPHY), CAST(ST_SetSRID(ST_MakePoint(:lon, :lat), 4326) AS GEOGRAPHY), :range)",
        nativeQuery = true)
    List<FoodPost> findNearbyPosts(double lat, double lon, int range,
                                   LocalDateTime after,
                                   List<Long> productIds);
}
