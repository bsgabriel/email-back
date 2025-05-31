package com.bsg.emailback.service.messages;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageSourceService {

    private final MessageSource messageSource;

    public String getMessage(String messaName) {
        return this.getMessage(messaName, (Object[]) null);
    }

    public String getMessage(String messaName, Object... args) {
        return this.messageSource.getMessage(messaName, args, LocaleContextHolder.getLocale());
    }
}
