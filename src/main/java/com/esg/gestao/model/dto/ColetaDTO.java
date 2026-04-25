package com.esg.gestao.model.dto;

import com.esg.gestao.model.enums.StatusColeta;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColetaDTO {
    private Long idColeta;

    @NotNull(message = "Data da coleta é obrigatória")
    private LocalDate dataColeta;

    @NotNull(message = "Status é obrigatório")
    private StatusColeta status;

    @NotNull(message = "ID do resíduo é obrigatório")
    private Long idResiduo;

    @NotNull(message = "ID do transportador é obrigatório")
    private Long idTransportador;

    private String tipoResiduo;
    private String nomeTransportador;
}