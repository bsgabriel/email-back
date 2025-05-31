package com.bsg.emailback.validator;

import com.bsg.emailback.exceptions.InvalidEmailException;
import com.bsg.emailback.service.messages.MessageSourceService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmailValidator {

    private final Validator validator;
    private final MessageSourceService messageSourceService;

    public void validate(Object mail) {
        var violations = validator.validate(mail);
        if (violations.isEmpty()) {
            return;
        }

        var errors = violations.stream()
                .map(this::getMessageForError)
                .collect(Collectors.toList());

        var errorMessage = this.messageSourceService.getMessage("mail.error.invalid-data");
        throw new InvalidEmailException(errorMessage, errors);
    }

    private String getMessageForError(ConstraintViolation<Object> violation) {
        String messageKey = violation.getMessage();

        if (messageKey.contains("exceeds")) {
            int maxSize = (int) violation.getConstraintDescriptor().getAttributes().get("max");
            return this.messageSourceService.getMessage(messageKey, maxSize);
        }

        return messageSourceService.getMessage(messageKey);
    }

}
