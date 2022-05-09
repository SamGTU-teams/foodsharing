package ru.rassafel.foodsharing.vkbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.bot.session.service.UserService;
import ru.rassafel.foodsharing.vkbot.model.domain.VkUser;
import ru.rassafel.foodsharing.vkbot.repository.VkUserRepository;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class VkUserService implements UserService {

    private final VkUserRepository repository;

    @Override
    public void saveUser(User user) {
        if(user instanceof VkUser){
            repository.save((VkUser) user);
        }else {
            throw new IllegalArgumentException("User is not from VK!");
        }
    }

    @Override
    public Optional<? extends User> getUser(Long id) {
        return repository.findById(id);
    }
}
