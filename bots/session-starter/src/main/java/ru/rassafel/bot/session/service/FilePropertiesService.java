package ru.rassafel.bot.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class FilePropertiesService {
    private final MessageSource messageSource;

    public String getSessionMessage(String code) {
        return messageSource.getMessage("session." + code, null, Locale.getDefault());
    }

    public String getButtonName(String code) {
        return messageSource.getMessage("button." + code, null, Locale.getDefault());
    }
}
