package com.bsg.emailback.validator;

import com.bsg.emailback.dto.EmailAwsDTO;
import com.bsg.emailback.dto.EmailOciDTO;
import com.bsg.emailback.exceptions.InvalidEmailException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailValidatorTest {

    @Autowired
    private EmailValidator emailValidator;

    @Test
    public void shouldPassValidation_whenAwsEmail_isValid() {
        // Arrange
        var validEmail = EmailAwsDTO.builder()
                .recipient("test@email.com")
                .recipientName("Test User")
                .sender("sender@email.com")
                .subject("Test Subject")
                .content("Test Content")
                .build();

        // Act/assert
        assertDoesNotThrow(() -> emailValidator.validate(validEmail));
    }

    @Test
    public void shouldThrowException_whenAwsEmailData_isNull() {
        var validEmail = EmailAwsDTO.builder().build();

        var excetpion = assertThrows(InvalidEmailException.class, () -> emailValidator.validate(validEmail));

        assertNotNull(excetpion.getErrors());
        assertFalse(excetpion.getErrors().isEmpty());
        assertEquals(5, excetpion.getErrors().size());
    }

    @Test
    public void shouldPassValidation_whenAwsEmailData_isInvalid() {
        var validEmail = EmailAwsDTO.builder()
                .recipient(randomAlphabetic(46))
                .recipientName(randomAlphabetic(61))
                .sender(randomAlphabetic(46))
                .subject(randomAlphabetic(121))
                .content(randomAlphabetic(257))
                .build();

        var excetpion = assertThrows(InvalidEmailException.class, () -> emailValidator.validate(validEmail));

        assertNotNull(excetpion.getErrors());
        assertFalse(excetpion.getErrors().isEmpty());
        assertEquals(5, excetpion.getErrors().size());
    }
    //</editor-fold>

    //<editor-fold desc="OCI Email validation">
    @Test
    public void shouldPassValidation_whenOciEmail_isValid() {
        var validEmail = EmailOciDTO.builder()
                .recipientEmail("test@email.com")
                .recipientName("Test User")
                .senderEmail("sender@email.com")
                .subject("Test Subject")
                .body("Test Content")
                .build();

        assertDoesNotThrow(() -> emailValidator.validate(validEmail));
    }

    @Test
    public void shouldThrowException_whenOciEmailData_isNull() {
        var validEmail = EmailOciDTO.builder().build();

        var excetpion = assertThrows(InvalidEmailException.class, () -> emailValidator.validate(validEmail));

        assertNotNull(excetpion.getErrors());
        assertFalse(excetpion.getErrors().isEmpty());
        assertEquals(5, excetpion.getErrors().size());
    }

    @Test
    public void shouldPassValidation_whenOciEmailData_isInvalid() {
        var validEmail = EmailOciDTO.builder()
                .recipientEmail(randomAlphabetic(41))
                .recipientName(randomAlphabetic(51))
                .senderEmail(randomAlphabetic(41))
                .subject(randomAlphabetic(101))
                .body(randomAlphabetic(251))
                .build();

        var excetpion = assertThrows(InvalidEmailException.class, () -> emailValidator.validate(validEmail));

        assertNotNull(excetpion.getErrors());
        assertFalse(excetpion.getErrors().isEmpty());
        assertEquals(5, excetpion.getErrors().size());
    }
    //</editor-fold>
}