package com.bsg.emailback.enums;

import com.bsg.emailback.service.email.AwsEmailService;
import com.bsg.emailback.service.email.EmailService;
import com.bsg.emailback.service.email.OciEmailService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum MailProvider {

    AWS(AwsEmailService.class),
    OCI(OciEmailService.class);

    private final Class<? extends EmailService<?>> serviceClass;

    public static MailProvider getValue(String integrationType) {
        return Arrays.stream(values())
                .filter(v -> v.name().equals(integrationType))
                .findFirst()
                .orElse(null);
    }

    public static String valuesAsString() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }


}
