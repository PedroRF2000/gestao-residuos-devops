package com.esg.gestao.repository;

import com.esg.gestao.model.entity.Residuo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ResiduoRepository extends JpaRepository<Residuo, Long> {
    List<Residuo> findByTipo(String tipo);
    List<Residuo> findByLocalIdLocal(Long idLocal);
    List<Residuo> findByDataGeracaoBetween(LocalDate inicio, LocalDate fim);

    @Query("SELECT r FROM Residuo r WHERE r.quantidade > :limite")
    List<Residuo> findByQuantidadeMaiorQue(@Param("limite") BigDecimal limite);

    @Query("SELECT r.tipo, SUM(r.quantidade) FROM Residuo r GROUP BY r.tipo")
    List<Object[]> findQuantidadePorTipo();
}