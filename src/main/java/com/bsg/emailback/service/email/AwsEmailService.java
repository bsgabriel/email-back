package com.bsg.emailback.service.email;

import com.bsg.emailback.dto.EmailAwsDTO;
import com.bsg.emailback.dto.EmailRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AwsEmailService extends EmailService<EmailAwsDTO> {

    @Override
    protected EmailAwsDTO mapToProviderDto(EmailRequestDTO request) {
        return EmailAwsDTO.builder()
                .recipient(request.getEmailDestinatario())
                .recipientName(request.getNomeDestinatario())
                .sender(request.getEmailRemetente())
                .subject(request.getAssunto())
                .content(request.getConteudo())
                .build();
    }

    @Override
    protected void processSerializedEmail(String serializedJson) {
        log.info("AWS email, {}", serializedJson);
    }
}
