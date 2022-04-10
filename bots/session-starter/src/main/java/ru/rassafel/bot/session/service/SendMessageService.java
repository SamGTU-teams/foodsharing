package ru.rassafel.bot.session.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendMessageService {

    @Value("${send-message.fixedDelay:3000}")
    private Long fixedDelay;
    private LocalDateTime lastExecuted = LocalDateTime.now();

    public synchronized void sendEvent(Runnable operation){
        long until = lastExecuted.until(LocalDateTime.now(), ChronoUnit.MILLIS);
        if(until < fixedDelay){
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
