package com.esg.gestao.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestinoFinalDTO {
    private Long idDestino;

    @NotBlank(message = "Tipo de destino é obrigatório")
    @Size(max = 50, message = "Tipo de destino deve ter no máximo 50 caracteres")
    private String tipoDestino;

    @NotBlank(message = "Local de destino é obrigatório")
    @Size(max = 150, message = "Local de destino deve ter no máximo 150 caracteres")
    private String localDestino;

    @NotNull(message = "Data de recebimento é obrigatória")
    private LocalDate dataRecebimento;

    @NotNull(message = "ID da coleta é obrigatório")
    private Long idColeta;
}