package ru.rassafel.bot.session.repository.user;

import org.springframework.data.repository.CrudRepository;
import ru.rassafel.bot.session.model.entity.user.User;

public interface AbstractUserRepository extends CrudRepository<User, Long> {
}
