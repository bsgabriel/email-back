package com.bsg.emailback.factory;

import com.bsg.emailback.enums.MailProvider;
import com.bsg.emailback.exceptions.EmailIntegrationNotConfiguredException;
import com.bsg.emailback.exceptions.InvalidEmailProviderException;
import com.bsg.emailback.service.email.EmailService;
import com.bsg.emailback.service.messages.MessageSourceService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@RequiredArgsConstructor
public class EmailServiceFactory {

    private final MessageSourceService messageSourceService;
    private final Map<String, EmailService<?>> emailServices;

    @Value("${mail.integracao}")
    private String integrationType;

    public EmailService<?> getEmailService() {
        if (isBlank(integrationType)) {
            var errorMEssage = this.messageSourceService.getMessage("email-integration-type.not-provided");
            throw new EmailIntegrationNotConfiguredException(errorMEssage);
        }

        var provider = MailProvider.getValue(integrationType);
        if (isNull(provider)) {
            var errorMEssage = this.messageSourceService.getMessage("email-integration-type.invalid", integrationType, MailProvider.valuesAsString());
            throw new InvalidEmailProviderException(errorMEssage);
        }

        var serviceName = resolveServiceNameByClass(provider.getServiceClass());
        return this.emailServices.get(serviceName);
    }

    private String resolveServiceNameByClass(Class<?> clz) {
        return StringUtils.uncapitalize(clz.getSimpleName());
    }
}
