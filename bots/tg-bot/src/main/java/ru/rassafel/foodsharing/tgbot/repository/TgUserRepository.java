package ru.rassafel.foodsharing.tgbot.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.rassafel.foodsharing.tgbot.model.TgUser;

import java.util.List;
import java.util.Optional;

public interface TgUserRepository extends JpaRepository<TgUser, Long> {
    @Query(nativeQuery = true,
        value = "SELECT tu.id, tp.name AS place_name, prod.name AS product_name FROM tg_bot.tg_user tu " +
            "INNER JOIN tg_bot.tg_place tp ON tu.id = tp.user_id " +
            "INNER JOIN tg_bot.tg_user_products tg_up ON tu.id = tg_up.user_id " +
            "INNER JOIN public.product prod ON tg_up.product_id = prod.id " +
            "WHERE prod.id IN (:productIds) " +
            "AND ST_DWithin(CAST(ST_SetSRID(ST_MakePoint(:lon, :lat), 4326) AS GEOGRAPHY), " +
            "CAST(ST_SetSRID(ST_MakePoint(tp.lon, tp.lat), 4326) AS GEOGRAPHY), tp.radius)")
    List<Object[]> findByProductAndSuitablePlace(List<Long> productIds, double lat, double lon);

    @EntityGraph(attributePaths = "products")
    Optional<TgUser> findWithProductsById(Long id);
}
