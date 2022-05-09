package ru.rassafel.foodsharing.tgbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.foodsharing.tgbot.model.TgUser;
import ru.rassafel.bot.session.service.UserService;
import ru.rassafel.foodsharing.tgbot.repository.TgUserRepository;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class TgUserService implements UserService {

    private final TgUserRepository repository;

    @Override
    public void saveUser(User user) {
        if(user instanceof TgUser){
            repository.save((TgUser) user);
        }else {
            throw new IllegalArgumentException("Provided user is not from TG!");
        }
    }

    @Override
    public Optional<User> getUser(Long id) {
        return repository.findById(id).map(Function.identity());
    }
}
