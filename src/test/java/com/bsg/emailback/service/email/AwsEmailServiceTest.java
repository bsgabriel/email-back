package com.bsg.emailback.service.email;

import com.bsg.emailback.dto.EmailAwsDTO;
import com.bsg.emailback.dto.EmailRequestDTO;
import com.bsg.emailback.exceptions.EmailSerializationException;
import com.bsg.emailback.service.messages.MessageSourceService;
import com.bsg.emailback.validator.EmailValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AwsEmailServiceTest {

    @InjectMocks
    private AwsEmailService awsEmailService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private EmailValidator emailValidator;

    @Mock
    private MessageSourceService messageSourceService;

    @Test
    void shouldCorrectlyMapEmailRequestDto_toEmailAwsDto() {
        // Arrange
        var emailRequest = createEmailRequest();

        // Act
        var result = awsEmailService.mapToProviderDto(emailRequest);

        // Assert
        assertEquals(emailRequest.getEmailDestinatario(), result.getRecipient());
        assertEquals(emailRequest.getNomeDestinatario(), result.getRecipientName());
        assertEquals(emailRequest.getEmailRemetente(), result.getSender());
        assertEquals(emailRequest.getAssunto(), result.getSubject());
        assertEquals(emailRequest.getConteudo(), result.getContent());
    }

    @Test
    void shouldSuccessfullySerialize_andProcessEmail() throws JsonProcessingException {
        // Arrange
        var emailRequest = createEmailRequest();
        String expectedJson = "{\"recipient\":\"destinatario@example.com\",\"recipientName\":\"João Silva\"}";
        when(objectMapper.writeValueAsString(any(EmailAwsDTO.class))).thenReturn(expectedJson);
        doNothing().when(emailValidator).validate(any(EmailAwsDTO.class));

        // Act
        awsEmailService.serializeAsJson(emailRequest);

        // Assert
        verify(emailValidator).validate(any(EmailAwsDTO.class));
        verify(objectMapper).writeValueAsString(any(EmailAwsDTO.class));
    }

    @Test
    void shouldThrowEmailSerializationException_whenJsonSerializationFails() throws JsonProcessingException {
        // Arrange
        var emailRequest = createEmailRequest();
        String errorMessage = "Serialization error occurred";
        JsonProcessingException jsonException = new JsonProcessingException("JSON error") {
        };

        when(objectMapper.writeValueAsString(any(EmailAwsDTO.class))).thenThrow(jsonException);
        when(messageSourceService.getMessage("mail.error.serialization")).thenReturn(errorMessage);
        doNothing().when(emailValidator).validate(any(EmailAwsDTO.class));

        // Act / assert
        assertThatThrownBy(() -> awsEmailService.serializeAsJson(emailRequest))
                .isInstanceOf(EmailSerializationException.class)
                .hasMessage(errorMessage)
                .hasCause(jsonException);
    }

    @Test
    void shouldThrowException() throws JsonProcessingException {
        // Arrange
        var emailRequest = createEmailRequest();
        var validationException = new EmailSerializationException("Validation failed", new Exception());
        doThrow(validationException).when(emailValidator).validate(any(EmailAwsDTO.class));

        // Act / assert
        assertThatThrownBy(() -> awsEmailService.serializeAsJson(emailRequest))
                .isSameAs(validationException);

        verify(emailValidator).validate(any(EmailAwsDTO.class));
        verify(objectMapper, times(0)).writeValueAsString(any());
    }

    @Test
    void shouldCallProcessSerializedEmail_withCorrectJson() throws JsonProcessingException {
        // Arrange
        var emailRequest = createEmailRequest();
        String expectedJson = "{\"recipient\":\"destinatario@example.com\"}";
        when(objectMapper.writeValueAsString(any(EmailAwsDTO.class))).thenReturn(expectedJson);
        doNothing().when(emailValidator).validate(any(EmailAwsDTO.class));

        // Act
        awsEmailService.serializeAsJson(emailRequest);

        // Assert
        verify(objectMapper).writeValueAsString(any(EmailAwsDTO.class));
    }

    private EmailRequestDTO createEmailRequest() {
        return EmailRequestDTO.builder()
                .emailDestinatario("destinatario@example.com")
                .nomeDestinatario("João Silva")
                .emailRemetente("remetente@example.com")
                .assunto("Teste de Email")
                .conteudo("Conteúdo do email de teste")
                .build();
    }

}