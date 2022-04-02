package ru.rassafel.foodsharing.common.repository.user;

import org.springframework.data.repository.CrudRepository;
import ru.rassafel.foodsharing.common.model.entity.user.TgUser;

public interface TgUserRepository extends CrudRepository<TgUser, Long> {
}
