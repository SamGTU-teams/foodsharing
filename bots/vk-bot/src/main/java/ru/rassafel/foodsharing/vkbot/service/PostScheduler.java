package ru.rassafel.foodsharing.vkbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.service.SendMessageService;
import ru.rassafel.foodsharing.vkbot.repository.VkUserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostScheduler {

    private final SendMessageService service;
    private final VkMessageSchedulerService vkMessageSchedulerService;
    private final VkUserRepository repository;


//    @Scheduled(fixedDelay = 5000)
    public void sendPost() {
        System.out.println("Trying to send message");
        //метро московская 53.20307254713856, 50.16069896267102
        double lon = 50.16069896267102;
        double lat = 53.20307254713856;
        service.sendEvent(() -> {
            List<Long> userIds = repository.findByProductAndSuitablePlace(List.of(1L), lat, lon)
                .stream().map(o -> (Long) o[0])
                .collect(Collectors.toList());
            if (userIds.isEmpty()) {
                return;
            }
            vkMessageSchedulerService.scheduleEvent("Тебе пост хуила",
                userIds.stream().map(Long::intValue).toArray(Integer[]::new));
        });
    }
}
