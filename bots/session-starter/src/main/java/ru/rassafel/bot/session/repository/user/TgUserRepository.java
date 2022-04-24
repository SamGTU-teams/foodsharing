package ru.rassafel.bot.session.repository.user;

import org.springframework.data.repository.CrudRepository;
import ru.rassafel.bot.session.model.entity.user.TgUser;

public interface TgUserRepository extends CrudRepository<TgUser, Long> {
}
