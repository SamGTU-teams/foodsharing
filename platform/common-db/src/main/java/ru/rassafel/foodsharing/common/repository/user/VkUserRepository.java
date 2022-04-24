package ru.rassafel.foodsharing.common.repository.user;

import org.springframework.data.repository.CrudRepository;
import ru.rassafel.foodsharing.common.model.entity.user.VkUser;

public interface VkUserRepository extends CrudRepository<VkUser, Long> {
}
