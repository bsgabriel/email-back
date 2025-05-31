package com.bsg.emailback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequestDTO {

    private String emailDestinatario;
    private String nomeDestinatario;
    private String emailRemetente;
    private String assunto;
    private String conteudo;

}
