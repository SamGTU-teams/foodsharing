package ru.rassafel.bot.session.mapper;

import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.entity.User;

public interface UserDtoMapper {
    User map(SessionRequest rq);
}
