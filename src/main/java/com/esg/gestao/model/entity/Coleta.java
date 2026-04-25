package com.esg.gestao.model.entity;

import com.esg.gestao.model.enums.StatusColeta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Entidade que representa uma Coleta de resíduos
 * Status: PENDENTE, EM_TRANSPORTE, FINALIZADA, CANCELADA
 */
@Entity
@Table(name = "COLETA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coleta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_coleta")
    @SequenceGenerator(name = "seq_coleta", sequenceName = "SEQ_COLETA", allocationSize = 1)
    @Column(name = "id_coleta")
    private Long idColeta;

    @Column(name = "data_coleta", nullable = false)
    private LocalDate dataColeta;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private StatusColeta status;

    // Relacionamento: Uma coleta é de um resíduo específico
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_residuo", nullable = false)
    private Residuo residuo;

    // Relacionamento: Uma coleta é realizada por um transportador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_transportador", nullable = false)
    private Transportador transportador;

    // Relacionamento: Uma coleta pode ter vários destinos finais
    @OneToMany(mappedBy = "coleta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DestinoFinal> destinosFinais;
}