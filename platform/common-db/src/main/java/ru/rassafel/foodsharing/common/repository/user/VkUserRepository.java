package ru.rassafel.foodsharing.common.repository.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.rassafel.foodsharing.common.model.entity.user.User;
import ru.rassafel.foodsharing.common.model.entity.user.VkUser;

import java.util.List;
import java.util.Optional;

public interface VkUserRepository extends CrudRepository<VkUser, Long> {

    @Query("select u.id from VkUser u")
    List<Long> findUserIds();

}
