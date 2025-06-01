package com.bsg.emailback.factory;

import com.bsg.emailback.enums.MailProvider;
import com.bsg.emailback.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailServiceFactory {

    private final Map<String, EmailService<?>> emailServices;

    @Value("${mail.integracao}")
    private String integrationType;

    public EmailService<?> getEmailService() {
        var provider = MailProvider.valueOf(integrationType);
        var serviceName = resolveServiceNameByClass(provider.getServiceClass());
        return this.emailServices.get(serviceName);
    }

    private String resolveServiceNameByClass(Class<?> clz) {
        return StringUtils.uncapitalize(clz.getSimpleName());
    }
}
