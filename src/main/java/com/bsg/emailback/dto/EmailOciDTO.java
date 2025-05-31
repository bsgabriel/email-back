package com.bsg.emailback.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailOciDTO {

    @NotBlank(message = "mail.recipient-email.not-provided")
    @Size(max = 40, message = "mail.recipient-email.exceeds-characters")
    private String recipientEmail;

    @NotBlank(message = "mail.recipient-name.not-provided")
    @Size(max = 50, message = "mail.recipient-name.exceeds-characters")
    private String recipientName;

    @NotBlank(message = "mail.sender-email.not-provided")
    @Size(max = 40, message = "mail.sender-email.exceeds-characters")
    private String senderEmail;

    @NotBlank(message = "mail.subject.not-provided")
    @Size(max = 100, message = "mail.subject.exceeds-characters")
    private String subject;

    @NotBlank(message = "mail.content.not-provided")
    @Size(max = 250, message = "mail.content.exceeds-characters")
    private String body;

}
