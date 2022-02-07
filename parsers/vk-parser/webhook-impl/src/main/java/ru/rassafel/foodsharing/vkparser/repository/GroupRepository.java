package ru.rassafel.foodsharing.vkparser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rassafel.foodsharing.vkparser.model.entity.VkGroup;

/**
 * @author rassafel
 */
public interface GroupRepository extends JpaRepository<VkGroup, Integer> {
}
