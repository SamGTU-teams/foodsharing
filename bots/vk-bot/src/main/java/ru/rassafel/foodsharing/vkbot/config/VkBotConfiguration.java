package ru.rassafel.foodsharing.vkbot.config;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Setter
@ConfigurationProperties(prefix = "vk.bot")
@Configuration
public class VkBotConfiguration {

    private int groupId;
    private String accessToken;

    @Bean
    public VkApiClient vkApiClient() {
        HttpTransportClient httpClient = HttpTransportClient.getInstance();
        return new VkApiClient(httpClient);
    }

    @Bean
    public GroupActor groupActor() {
        return new GroupActor(groupId, accessToken);
    }
}
