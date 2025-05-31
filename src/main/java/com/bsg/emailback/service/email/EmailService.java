package com.bsg.emailback.service.email;

import com.bsg.emailback.dto.EmailRequestDTO;
import com.bsg.emailback.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class EmailService<T> {

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
        // TODO: implementar
        return "";
    }
}