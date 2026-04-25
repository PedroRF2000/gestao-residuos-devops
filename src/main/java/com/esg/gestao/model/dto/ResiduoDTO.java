package com.esg.gestao.model.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResiduoDTO {
    private Long idResiduo;

    @NotBlank(message = "Tipo é obrigatório")
    @Size(max = 50, message = "Tipo deve ter no máximo 50 caracteres")
    private String tipo;

    @NotNull(message = "Quantidade é obrigatória")
    @DecimalMin(value = "0.01", message = "Quantidade deve ser maior que zero")
    private BigDecimal quantidade;

    @NotNull(message = "Data de geração é obrigatória")
    @PastOrPresent(message = "Data de geração não pode ser futura")
    private LocalDate dataGeracao;

    @NotNull(message = "ID do local é obrigatório")
    private Long idLocal;

    private String nomeLocal;
}