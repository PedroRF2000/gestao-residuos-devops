package com.esg.gestao.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entidade que representa um Transportador de resíduos
 * Deve possuir licença ambiental válida para realizar coletas
 */
@Entity
@Table(name = "TRANSPORTADOR")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transportador {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_transportador")
    @SequenceGenerator(name = "seq_transportador", sequenceName = "SEQ_TRANSPORTADOR", allocationSize = 1)
    @Column(name = "id_transportador")
    private Long idTransportador;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "cnpj", length = 20, unique = true, nullable = false)
    private String cnpj;

    @Column(name = "licenca_ambiental", length = 50)
    private String licencaAmbiental;

    // Relacionamento: Um transportador pode fazer várias coletas
    @OneToMany(mappedBy = "transportador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Coleta> coletas;
}