package com.esg.gestao.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entidade que representa o Destino Final de um resíduo
 * Criado automaticamente quando uma coleta é finalizada (trigger)
 * Tipos: Reciclagem, Aterro Sanitário, Compostagem, Incineração, etc.
 */
@Entity
@Table(name = "DESTINO_FINAL")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestinoFinal {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_destino")
    @SequenceGenerator(name = "seq_destino", sequenceName = "SEQ_DESTINO", allocationSize = 1)
    @Column(name = "id_destino")
    private Long idDestino;

    @Column(name = "tipo_destino", length = 50, nullable = false)
    private String tipoDestino;

    @Column(name = "local_destino", length = 150, nullable = false)
    private String localDestino;

    @Column(name = "data_recebimento", nullable = false)
    private LocalDate dataRecebimento;

    // Relacionamento: Um destino final pertence a uma coleta
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_coleta", nullable = false)
    private Coleta coleta;
}