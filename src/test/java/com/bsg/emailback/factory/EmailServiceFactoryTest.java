package com.bsg.emailback.factory;

import com.bsg.emailback.exceptions.EmailIntegrationNotConfiguredException;
import com.bsg.emailback.exceptions.InvalidEmailProviderException;
import com.bsg.emailback.service.email.AwsEmailService;
import com.bsg.emailback.service.email.EmailService;
import com.bsg.emailback.service.email.OciEmailService;
import com.bsg.emailback.service.messages.MessageSourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceFactoryTest {

    @InjectMocks
    private EmailServiceFactory emailServiceFactory;

    @Mock
    private MessageSourceService messageSourceService;

    @Mock
    private EmailService<AwsEmailService> awsEmailService;

    @Mock
    private EmailService<OciEmailService> ociEmailService;

    @BeforeEach
    void setUp() {
        Map<String, EmailService<?>> emailServices = Map.of(
                "awsEmailService", awsEmailService,
                "ociEmailService", ociEmailService
        );

        emailServiceFactory = new EmailServiceFactory(messageSourceService, emailServices);
    }

    @Test
    void shouldReturnAwsEmailService_whenIntegrationType_isAws() {
        ReflectionTestUtils.setField(emailServiceFactory, "integrationType", "AWS");

        EmailService<?> result = emailServiceFactory.getEmailService();

        assertThat(result).isSameAs(awsEmailService);
    }

    @Test
    void shouldReturnOciEmailService_whenIntegrationType_isOci() {
        ReflectionTestUtils.setField(emailServiceFactory, "integrationType", "OCI");

        EmailService<?> result = emailServiceFactory.getEmailService();

        assertThat(result).isSameAs(ociEmailService);
    }

    @Test
    void shouldThrowException_whenIntegrationType_isNull() {
        ReflectionTestUtils.setField(emailServiceFactory, "integrationType", null);
        String expectedErrorMessage = "Integration type not provided";
        when(messageSourceService.getMessage("email-integration-type.not-provided"))
                .thenReturn(expectedErrorMessage);

        assertThatThrownBy(() -> emailServiceFactory.getEmailService())
                .isInstanceOf(EmailIntegrationNotConfiguredException.class)
                .hasMessage(expectedErrorMessage);
    }

    @Test
    void shouldThrowException_whenIntegrationType_isEmpty() {
        ReflectionTestUtils.setField(emailServiceFactory, "integrationType", "");
        String expectedErrorMessage = "Integration type not provided";
        when(messageSourceService.getMessage("email-integration-type.not-provided"))
                .thenReturn(expectedErrorMessage);

        assertThatThrownBy(() -> emailServiceFactory.getEmailService())
                .isInstanceOf(EmailIntegrationNotConfiguredException.class)
                .hasMessage(expectedErrorMessage);
    }

    @Test
    void shouldThrowException_whenIntegrationType_isBlank() {
        ReflectionTestUtils.setField(emailServiceFactory, "integrationType", "   ");
        String expectedErrorMessage = "Integration type not provided";
        when(messageSourceService.getMessage("email-integration-type.not-provided"))
                .thenReturn(expectedErrorMessage);

        assertThatThrownBy(() -> emailServiceFactory.getEmailService())
                .isInstanceOf(EmailIntegrationNotConfiguredException.class)
                .hasMessage(expectedErrorMessage);
    }

    @Test
    void shouldThrowException_whenIntegrationType_isInvalid() {
        // Given
        String invalidIntegrationType = "ABC";
        ReflectionTestUtils.setField(emailServiceFactory, "integrationType", invalidIntegrationType);
        String expectedErrorMessage = "Invalid email provider: ABC. Valid providers: AWS, OCI";
        when(messageSourceService.getMessage("email-integration-type.invalid", invalidIntegrationType, "AWS, OCI"))
                .thenReturn(expectedErrorMessage);

        assertThatThrownBy(() -> emailServiceFactory.getEmailService())
                .isInstanceOf(InvalidEmailProviderException.class)
                .hasMessage(expectedErrorMessage);
    }

    @Test
    void shouldThrowException_whenIntegrationType_hasDifferentCase() {
        String invalidIntegrationType = "aws";
        ReflectionTestUtils.setField(emailServiceFactory, "integrationType", invalidIntegrationType);
        String expectedErrorMessage = "Invalid email provider: aws. Valid providers: AWS, OCI";
        when(messageSourceService.getMessage("email-integration-type.invalid", invalidIntegrationType, "AWS, OCI"))
                .thenReturn(expectedErrorMessage);

        assertThatThrownBy(() -> emailServiceFactory.getEmailService())
                .isInstanceOf(InvalidEmailProviderException.class)
                .hasMessage(expectedErrorMessage);
    }

    @Test
    void shouldReturnNull_whenEmailService_notFoundInMap() {
        Map<String, EmailService<?>> emptyEmailServices = Map.of();
        EmailServiceFactory factoryWithEmptyMap = new EmailServiceFactory(messageSourceService, emptyEmailServices);
        ReflectionTestUtils.setField(factoryWithEmptyMap, "integrationType", "AWS");

        EmailService<?> result = factoryWithEmptyMap.getEmailService();

        assertThat(result).isNull();
    }

    @Test
    void shouldVerifyServiceNameResolution_followsCorrectNamingConvention_forAwsService() {
        ReflectionTestUtils.setField(emailServiceFactory, "integrationType", "AWS");

        EmailService<?> result = emailServiceFactory.getEmailService();

        assertThat(result).isSameAs(awsEmailService);
    }

    @Test
    void shouldVerifyServiceNameResolution_followsCorrectNamingConvention_forOciService() {
        ReflectionTestUtils.setField(emailServiceFactory, "integrationType", "OCI");

        EmailService<?> result = emailServiceFactory.getEmailService();

        assertThat(result).isSameAs(ociEmailService);
    }
}