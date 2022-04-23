package ru.rassafel.foodsharing.common.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rassafel.foodsharing.common.repository.user.AbstractUserRepository;
import ru.rassafel.foodsharing.common.repository.user.TgUserRepository;
import ru.rassafel.foodsharing.common.model.PlatformType;
import ru.rassafel.foodsharing.common.model.entity.user.User;
import ru.rassafel.foodsharing.common.repository.user.VkUserRepository;

import java.util.Optional;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class UserService {

    private final AbstractUserRepository userRepository;
    private final TgUserRepository tgUserRepository;
    private final VkUserRepository vkUserRepository;

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public Optional<User> getUser(Long id, PlatformType type){
        if(type == PlatformType.TG){
            return tgUserRepository.findById(id).map(Function.identity());
        }
        else if(type == PlatformType.VK){
            return vkUserRepository.findById(id).map(Function.identity());
        }else {
            throw new RuntimeException("Not found PlatformType");
        }
    }

}
