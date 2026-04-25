package com.esg.gestao.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Entidade que representa um Resíduo gerado
 * Tipos: Plástico, Metal, Vidro, Papel, Orgânico, Eletrônico, etc.
 */
@Entity
@Table(name = "RESIDUO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Residuo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_residuo")
    @SequenceGenerator(name = "seq_residuo", sequenceName = "SEQ_RESIDUO", allocationSize = 1)
    @Column(name = "id_residuo")
    private Long idResiduo;

    @Column(name = "tipo", length = 50, nullable = false)
    private String tipo;

    @Column(name = "quantidade", precision = 10, scale = 2, nullable = false)
    private BigDecimal quantidade;

    @Column(name = "data_geracao", nullable = false)
    private LocalDate dataGeracao;

    // Relacionamento: Um resíduo pertence a um local
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_local", nullable = false)
    private Local local;

    // Relacionamento: Um resíduo pode ter várias coletas
    @OneToMany(mappedBy = "residuo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Coleta> coletas;
}