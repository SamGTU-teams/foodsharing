package ru.rassafel.foodsharing.session.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.service.Messenger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@RequiredArgsConstructor
@Slf4j
public class BatchMessenger implements Messenger {
    private final Messenger messenger;
    private final BlockingQueue<SessionResponse> queue;
    private final int maxQuerySizeInBatch;

    @Async("sendQueryTaskScheduler")
    @Scheduled(fixedRateString =
        "#{sessionConfig.properties.messenger.maxTimeForSendSomeQueries / " +
            "sessionConfig.properties.messenger.maxQueryCountPerTime}")
    public void sendScheduled() {
        List<SessionResponse> requests = new ArrayList<>();
        if (queue.drainTo(requests, maxQuerySizeInBatch) < 1) {
            return;
        }
        messenger.send(requests);
    }

    @Override
    public final void send(SessionResponse response) {
        try {
            queue.put(response);
        } catch (InterruptedException e) {
            log.error("", e);
        }
    }

    @Override
    public final void send(List<SessionResponse> responses) {
        Messenger.super.send(responses);
    }
}
