package ru.rassafel.foodsharing.session.model.mapper;

import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.entity.User;

public interface UserDtoMapper {
    User map(SessionRequest rq);
}
