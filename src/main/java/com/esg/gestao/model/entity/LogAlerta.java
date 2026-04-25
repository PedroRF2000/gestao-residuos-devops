package com.esg.gestao.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entidade que representa um Log de Alerta do sistema
 * Usado para registrar alertas sobre coletas pendentes, problemas, etc.
 */
@Entity
@Table(name = "LOG_ALERTA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogAlerta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_alerta")
    @SequenceGenerator(name = "seq_alerta", sequenceName = "SEQ_ALERTA", allocationSize = 1)
    @Column(name = "id_alerta")
    private Long idAlerta;

    @Column(name = "mensagem", length = 200, nullable = false)
    private String mensagem;

    @Column(name = "data_alerta", nullable = false)
    private LocalDate dataAlerta;
}