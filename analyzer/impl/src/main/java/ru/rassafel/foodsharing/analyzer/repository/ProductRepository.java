package ru.rassafel.foodsharing.analyzer.repository;

import org.springframework.data.repository.CrudRepository;
import ru.rassafel.foodsharing.common.model.entity.Product;

import java.util.stream.Stream;

/**
 * @author rassafel
 */
public interface ProductRepository extends CrudRepository<Product, Long> {
    Stream<Product> findByNameIsNotNull();
}
