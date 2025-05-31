package com.bsg.emailback.util;

import com.bsg.emailback.dto.ErrorDTO;
import com.bsg.emailback.exceptions.EmailSerializationException;
import com.bsg.emailback.exceptions.InvalidEmailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(EmailSerializationException.class)
    public ResponseEntity<ErrorDTO> handleEmailSerializationException(final EmailSerializationException ex) {
        log.error("Error while serializating email data", ex);
        return ResponseEntity.internalServerError().body(ErrorDTO.builder()
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ErrorDTO> handleInvalidEmailException(final InvalidEmailException ex) {
        log.error("Email contains invalid data", ex);
        return ResponseEntity.badRequest().body(ErrorDTO.builder()
                .message(ex.getMessage())
                .errors(ex.getErrors())
                .build());
    }
}