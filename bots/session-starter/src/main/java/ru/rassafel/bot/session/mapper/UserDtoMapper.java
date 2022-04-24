package ru.rassafel.bot.session.mapper;

import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.foodsharing.common.model.PlatformType;
import ru.rassafel.foodsharing.common.model.entity.user.User;
import ru.rassafel.foodsharing.common.model.entity.user.TgUser;
import ru.rassafel.foodsharing.common.model.entity.user.VkUser;

@Component
public class UserDtoMapper extends OrikaMapper {
    @Override
    public void configure() {
    }

    public User map(SessionRequest rq) {
        if(rq.getType() == PlatformType.TG){
            TgUser tgUser = new TgUser();
            tgUser.setId(rq.getFrom().getId());
            return tgUser;
        }
        else {
            VkUser vkUser = new VkUser();
            vkUser.setId(rq.getFrom().getId());
            return vkUser;
        }
    }
}
