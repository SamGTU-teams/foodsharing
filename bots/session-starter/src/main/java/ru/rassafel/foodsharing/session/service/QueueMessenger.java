package ru.rassafel.foodsharing.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@Service
@RequiredArgsConstructor
public class QueueMessenger {

    private final Messenger messenger;

    private final BlockingQueue<SessionResponse> queue;
    @Value("${botConfig.properties.client.maxQuerySizeInBatch:25}")
    private int maxQuerySizeInBatch;

    @Async("sendQueryTaskScheduler")
    @Scheduled(fixedRateString =
        "#{sessionConfig.properties.messenger.maxTimeForSendSomeQueries / " +
            "sessionConfig.properties.messenger.maxQueryCountPerTime}")
    public void sendScheduled() {
        List<SessionResponse> requests = new ArrayList<>();
        queue.drainTo(requests, maxQuerySizeInBatch);
        if (requests.isEmpty()) {
            return;
        }
        messenger.sendBatch(requests);
    }

}
