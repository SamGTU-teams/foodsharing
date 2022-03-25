package ru.rassafel.foodsharing.analyzer.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.rassafel.foodsharing.common.model.entity.Product;

/**
 * @author rassafel
 */
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
}
