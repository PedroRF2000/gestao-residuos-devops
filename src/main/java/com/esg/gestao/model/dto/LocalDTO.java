package com.esg.gestao.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalDTO {
    private Long idLocal;

    @NotBlank(message = "Nome do local é obrigatório")
    @Size(max = 100, message = "Nome do local deve ter no máximo 100 caracteres")
    private String nomeLocal;

    @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres")
    private String endereco;
}