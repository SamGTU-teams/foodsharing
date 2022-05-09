package ru.rassafel.bot.session.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.TimedSemaphore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendMessageService {
    @Value("${send-message.fixedDelay:3000}")
    private Integer fixedDelay;
    @Value("${send-message.maxCountPerDelay:25}")
    private Integer maxCountPerDelay;
    private LocalDateTime lastExecuted = LocalDateTime.now();

    public synchronized void sendEvent(Runnable operation) {
        long until = lastExecuted.until(LocalDateTime.now(), ChronoUnit.MILLIS);
        if (until < fixedDelay) {
            try {
                Thread.sleep(fixedDelay - until);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
        operation.run();
        lastExecuted = LocalDateTime.now();
    }
}
