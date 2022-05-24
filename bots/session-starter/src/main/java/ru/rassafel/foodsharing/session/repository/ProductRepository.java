package ru.rassafel.foodsharing.session.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rassafel.foodsharing.common.model.entity.product.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByNameEqualsIgnoreCase(String name);
}
