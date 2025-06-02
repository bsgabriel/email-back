package com.bsg.emailback.service.email;

import com.bsg.emailback.dto.EmailOciDTO;
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
class OciEmailServiceTest {

    @InjectMocks
    private OciEmailService ociEmailService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private EmailValidator emailValidator;

    @Mock
    private MessageSourceService messageSourceService;

    @Test
    void shouldCorrectlyMapEmailRequestDto_toEmailOciDto() {
        // Arrange
        var emailRequest = createEmailRequest();

        // Act
        var result = ociEmailService.mapToProviderDto(emailRequest);

        // Assert
        assertEquals(emailRequest.getEmailDestinatario(), result.getRecipientEmail());
        assertEquals(emailRequest.getNomeDestinatario(), result.getRecipientName());
        assertEquals(emailRequest.getEmailRemetente(), result.getSenderEmail());
        assertEquals(emailRequest.getAssunto(), result.getSubject());
        assertEquals(emailRequest.getConteudo(), result.getBody());
    }

    @Test
    void shouldSuccessfullySerialize_andProcessEmail() throws JsonProcessingException {
        // Arrange
        var emailRequest = createEmailRequest();
        String expectedJson = "{\"recipientEmail\":\"destinatario@example.com\",\"recipientName\":\"João Silva\"}";
        when(objectMapper.writeValueAsString(any(EmailOciDTO.class))).thenReturn(expectedJson);
        doNothing().when(emailValidator).validate(any(EmailOciDTO.class));

        // Act
        ociEmailService.serializeAsJson(emailRequest);

        // Assert
        verify(emailValidator).validate(any(EmailOciDTO.class));
        verify(objectMapper).writeValueAsString(any(EmailOciDTO.class));
    }

    @Test
    void shouldThrowEmailSerializationException_whenJsonSerializationFails() throws JsonProcessingException {
        // Arrange
        var emailRequest = createEmailRequest();
        String errorMessage = "Serialization error occurred";
        JsonProcessingException jsonException = new JsonProcessingException("JSON error") {
        };

        when(objectMapper.writeValueAsString(any(EmailOciDTO.class))).thenThrow(jsonException);
        when(messageSourceService.getMessage("mail.error.serialization")).thenReturn(errorMessage);
        doNothing().when(emailValidator).validate(any(EmailOciDTO.class));

        // Act / assert
        assertThatThrownBy(() -> ociEmailService.serializeAsJson(emailRequest))
                .isInstanceOf(EmailSerializationException.class)
                .hasMessage(errorMessage)
                .hasCause(jsonException);
    }

    @Test
    void shouldThrowException() throws JsonProcessingException {
        // Arrange
        var emailRequest = createEmailRequest();
        var validationException = new EmailSerializationException("Validation failed", new Exception());
        doThrow(validationException).when(emailValidator).validate(any(EmailOciDTO.class));

        // Act / assert
        assertThatThrownBy(() -> ociEmailService.serializeAsJson(emailRequest))
                .isSameAs(validationException);

        verify(emailValidator).validate(any(EmailOciDTO.class));
        verify(objectMapper, times(0)).writeValueAsString(any());
    }

    @Test
    void shouldCallProcessSerializedEmail_withCorrectJson() throws JsonProcessingException {
        // Arrange
        var emailRequest = createEmailRequest();
        String expectedJson = "{\"recipientEmail\":\"destinatario@example.com\"}";
        when(objectMapper.writeValueAsString(any(EmailOciDTO.class))).thenReturn(expectedJson);
        doNothing().when(emailValidator).validate(any(EmailOciDTO.class));

        // Act
        ociEmailService.serializeAsJson(emailRequest);

        // Assert
        verify(objectMapper).writeValueAsString(any(EmailOciDTO.class));
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