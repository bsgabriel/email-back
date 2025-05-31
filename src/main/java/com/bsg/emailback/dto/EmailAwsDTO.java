package com.bsg.emailback.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailAwsDTO {

    @NotBlank(message = "mail.recipient-email.not-provided")
    @Size(max = 45, message = "mail.recipient-email.exceeds-characters")
    private String recipient;

    @NotBlank(message = "mail.recipient-name.not-provided")
    @Size(max = 60, message = "mail.recipient-name.exceeds-characters")
    private String recipientName;

    @NotBlank(message = "mail.sender-email.not-provided")
    @Size(max = 45, message = "mail.sender-email.exceeds-characters")
    private String sender;

    @NotBlank(message = "mail.subject.not-provided")
    @Size(max = 120, message = "mail.subject.exceeds-characters")
    private String subject;

    @NotBlank(message = "mail.content.not-provided")
    @Size(max = 256, message = "mail.content.exceeds-characters")
    private String content;

}
