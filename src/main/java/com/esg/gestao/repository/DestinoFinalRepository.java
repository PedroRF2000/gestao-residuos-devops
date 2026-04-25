package com.esg.gestao.repository;

import com.esg.gestao.model.entity.DestinoFinal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinoFinalRepository extends JpaRepository<DestinoFinal, Long> {
    List<DestinoFinal> findByTipoDestino(String tipoDestino);
    List<DestinoFinal> findByColetaIdColeta(Long idColeta);

    @Query("SELECT d.tipoDestino, COUNT(d) FROM DestinoFinal d GROUP BY d.tipoDestino")
    List<Object[]> findQuantidadePorTipoDestino();
}