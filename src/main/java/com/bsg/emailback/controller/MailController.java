package com.bsg.emailback.controller;

import com.bsg.emailback.dto.EmailRequestDTO;
import com.bsg.emailback.factory.ServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private final ServiceFactory serviceFactory;

    @PostMapping
    public ResponseEntity<Void> serializeEmail(@RequestBody EmailRequestDTO body) {
        this.serviceFactory.getEmailService().serializeAsJson(body);
        return ResponseEntity.noContent().build();
    }
}
