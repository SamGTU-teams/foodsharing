package ru.rassafel.bot.session.service;

import ru.rassafel.bot.session.model.entity.User;

import java.util.Optional;

public interface UserService {
    void saveUser(User user);

    Optional<? extends User> getUser(Long id);
}
