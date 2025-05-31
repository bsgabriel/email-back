package com.bsg.emailback.service.email;

import com.bsg.emailback.dto.EmailOciDTO;
import com.bsg.emailback.dto.EmailRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OciEmailService extends EmailService<EmailOciDTO> {

    @Override
    protected EmailOciDTO mapToProviderDto(EmailRequestDTO request) {
        return EmailOciDTO.builder()
                .recipientEmail(request.getEmailDestinatario())
                .recipientName(request.getNomeDestinatario())
                .senderEmail(request.getEmailRemetente())
                .subject(request.getAssunto())
                .body(request.getConteudo())
                .build();
    }

    @Override
    protected void processSerializedEmail(String serializedJson) {
        log.info("OCI email, {}", serializedJson);
    }
}
