package ru.rassafel.foodsharing.vkbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.bot.session.service.UserService;
import ru.rassafel.foodsharing.vkbot.model.domain.VkUser;
import ru.rassafel.foodsharing.vkbot.repository.VkUserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VkUserService implements UserService {
    private final VkUserRepository repository;

    @Override
    public void saveUser(User user) {
        if (!(user instanceof VkUser)) {
            throw new IllegalArgumentException("User is not from VK!");
        }
        repository.save((VkUser) user);
    }

    @Override
    public Optional<? extends User> getUser(Long id) {
        return repository.findById(id);
    }
}
