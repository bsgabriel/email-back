package com.bsg.emailback.service.email;

import com.bsg.emailback.dto.EmailRequestDTO;

public abstract class EmailService<T> {

    protected abstract T mapToProviderDto(EmailRequestDTO request);

    protected abstract void processSerializedEmail(String serializedJson);

    public final void serializeAsJson(EmailRequestDTO request) {
        var providerDto = mapToProviderDto(request);
        // TODO: validar objeto mapeado

        processSerializedEmail(serializeToJson(providerDto));
    }

    protected String serializeToJson(T providerDto) {
        // TODO: implementar
        return "";
    }
}