package com.esg.gestao.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entidade que representa um Local de geração de resíduos
 * Exemplos: Fábrica, Armazém, Escritório, etc.
 */
@Entity
@Table(name = "LOCAL")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Local {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_local")
    @SequenceGenerator(name = "seq_local", sequenceName = "SEQ_LOCAL", allocationSize = 1)
    @Column(name = "id_local")
    private Long idLocal;

    @Column(name = "nome_local", length = 100, nullable = false)
    private String nomeLocal;

    @Column(name = "endereco", length = 200)
    private String endereco;

    // Relacionamento: Um local pode ter vários resíduos
    @OneToMany(mappedBy = "local", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Residuo> residuos;
}