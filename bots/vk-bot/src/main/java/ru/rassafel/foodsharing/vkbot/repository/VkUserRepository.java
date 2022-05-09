package ru.rassafel.foodsharing.vkbot.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.rassafel.foodsharing.vkbot.model.domain.VkUser;

import java.util.List;

public interface VkUserRepository extends CrudRepository<VkUser, Long> {

    @Query("select u.id from VkUser u")
    List<Long> findUserIds();

    @Query(nativeQuery = true,
        value = "SELECT DISTINCT us.id FROM vk_bot.vk_user us " +
            "INNER JOIN vk_bot.vk_place vp ON us.id = vp.user_id " +
            "INNER JOIN vk_bot.vk_user_products vk_prod ON us.id = vk_prod.user_id " +
            "INNER JOIN public.product prod ON vk_prod.product_id = prod.id " +
            "WHERE prod.id IN (:productIds) " +
            "AND ST_DWithin(CAST(ST_SetSRID(ST_MakePoint(:lon, :lat), 4326) AS GEOGRAPHY), " +
            "CAST(ST_SetSRID(ST_MakePoint(vp.lon, vp.lat), 4326) AS GEOGRAPHY), vp.radius)")
    List<Long> findByProductAndSuitablePlace(List<Long> productIds, double lat, double lon);

}