package com.bsg.emailback.validator;

import com.bsg.emailback.exceptions.InvalidEmailException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmailValidator {

    private final Validator validator;

    public void validate(Object mail) {
        var violations = validator.validate(mail);
        if (violations.isEmpty()) {
            return;
        }

        var errors = violations.stream()
                .map(this::getMessageForError)
                .collect(Collectors.toList());

        // TODO: dinamizar busca de mensgens
        throw new InvalidEmailException("email invalido", errors);
    }

    private String getMessageForError(ConstraintViolation<Object> violation) {
        // TODO: dinamizar busca de mensgens
        return violation.getMessage();
    }

}
