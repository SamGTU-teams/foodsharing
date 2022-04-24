package ru.rassafel.bot.session.mapper;

import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.model.entity.user.TgUser;
import ru.rassafel.bot.session.model.entity.user.User;
import ru.rassafel.bot.session.model.entity.user.VkUser;
import ru.rassafel.foodsharing.common.model.PlatformType;

@Component
public class UserDtoMapper {
    public User map(SessionRequest rq) {
        if (rq.getType() == PlatformType.TG) {
            TgUser tgUser = new TgUser();
            tgUser.setId(rq.getFrom().getId());
            return tgUser;
        } else {
            VkUser vkUser = new VkUser();
            vkUser.setId(rq.getFrom().getId());
            return vkUser;
        }
    }
}
