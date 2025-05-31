package com.bsg.emailback.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class InvalidEmailException extends RuntimeException {

    private final List<String> errors;

    public InvalidEmailException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

}
