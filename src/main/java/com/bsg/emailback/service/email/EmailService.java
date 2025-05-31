package com.bsg.emailback.service.email;

import com.bsg.emailback.dto.EmailRequestDTO;
import com.bsg.emailback.exceptions.EmailSerializationException;
import com.bsg.emailback.validator.EmailValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class EmailService<T> {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected EmailValidator emailValidator;

    protected abstract T mapToProviderDto(EmailRequestDTO request);

    protected abstract void processSerializedEmail(String serializedJson);

    public final void serializeAsJson(EmailRequestDTO request) {
        var providerDto = mapToProviderDto(request);
        this.emailValidator.validate(providerDto);

        processSerializedEmail(serializeToJson(providerDto));
    }

    protected String serializeToJson(T providerDto) {
        try {
            return objectMapper.writeValueAsString(providerDto);
        } catch (JsonProcessingException e) {
            throw new EmailSerializationException("Erro ao serializar", e);
        }
    }
}