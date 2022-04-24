package ru.rassafel.bot.session.repository.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.rassafel.bot.session.model.entity.user.VkUser;

import java.util.List;

public interface VkUserRepository extends CrudRepository<VkUser, Long> {

    @Query("select u.id from VkUser u")
    List<Long> findUserIds();

}
