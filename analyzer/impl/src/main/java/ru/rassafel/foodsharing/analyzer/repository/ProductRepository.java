package ru.rassafel.foodsharing.analyzer.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import ru.rassafel.foodsharing.common.model.entity.product.Product;

/**
 * @author rassafel
 */
public interface ProductRepository extends CrudRepository<Product, Long> {
    @Override
    Streamable<Product> findAll();
}
