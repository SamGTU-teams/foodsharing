package ru.rassafel.bot.session.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.rassafel.foodsharing.common.model.entity.product.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p where lower(p.name) = lower(:name)")
    Optional<Product> findByName(String name);
}
