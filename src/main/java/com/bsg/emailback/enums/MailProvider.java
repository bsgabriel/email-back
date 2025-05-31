package com.bsg.emailback.enums;

import com.bsg.emailback.service.email.AwsEmailService;
import com.bsg.emailback.service.email.EmailService;
import com.bsg.emailback.service.email.OciEmailService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MailProvider {

    AWS(AwsEmailService.class),
    OCI(OciEmailService.class);

    private final Class<? extends EmailService<?>> serviceClass;

}
