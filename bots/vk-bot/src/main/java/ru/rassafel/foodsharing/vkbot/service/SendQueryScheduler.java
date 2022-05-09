package ru.rassafel.foodsharing.vkbot.service;

import com.vk.api.sdk.client.AbstractQueryBuilder;
import com.vk.api.sdk.client.ClientResponse;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.queries.execute.ExecuteBatchQuery;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.vkbot.config.VkApiConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendQueryScheduler {

    private final BlockingQueue<AbstractQueryBuilder> queue;

    private final VkApiClient vk;
    private final GroupActor groupActor;
    @Value("${vk.api.maxQuerySizeInBatch:25}")
    private int maxQuerySizeInBatch;

    @Async("sendVkQueryTaskScheduler")
    @Scheduled(fixedRateString =
        "#{vkApiConfiguration.maxTimeForSendSomeQueries / vkApiConfiguration.maxQueryCountPerTime}")
    public void sendScheduled() {
        try {
            List<AbstractQueryBuilder> requests = new ArrayList<>();
            queue.drainTo(requests, maxQuerySizeInBatch);
            if (requests.isEmpty()) {
                return;
            }
            ClientResponse clientRs = vk.execute()
                .batch(groupActor, requests)
                .executeAsRaw();
            ofNullable(clientRs)
                .ifPresent(rs -> {
                    log.debug("Sent event to VK with response code : {} and body : {}",
                        clientRs.getStatusCode(), clientRs.getContent());
                });
        } catch (ClientException e) {
            log.error("Exception during send execute query to VK : {}", e.getMessage());
        }
    }


}
