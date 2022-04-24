package ru.rassafel.foodsharing.vkbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.service.SendMessageService;
import ru.rassafel.bot.session.repository.user.VkUserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostScheduler {

    private final SendMessageService service;
    private final VkMessengerService vkMessengerService;
    private final VkUserRepository repository;


//    @Scheduled(fixedDelay = 100)
    public void sendPost(){
        service.sendEvent(() -> {
            List<Long> userIds = repository.findUserIds();
            if(userIds.isEmpty()){
                return;
            }
            vkMessengerService.sendMessage("Приветос",
                userIds.stream().map(Long::intValue).collect(Collectors.toList()));
        });
    }

}
