package com.esg.gestao.repository;

import com.esg.gestao.model.entity.Coleta;
import com.esg.gestao.model.enums.StatusColeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ColetaRepository extends JpaRepository<Coleta, Long> {
    List<Coleta> findByStatus(StatusColeta status);
    List<Coleta> findByDataColetaBetween(LocalDate inicio, LocalDate fim);
    List<Coleta> findByTransportadorIdTransportador(Long idTransportador);

    @Query("SELECT c FROM Coleta c WHERE c.status = :status AND c.dataColeta < :dataLimite")
    List<Coleta> findColetasPendentesAtrasadas(
            @Param("status") StatusColeta status,
            @Param("dataLimite") LocalDate dataLimite
    );

    @Query("SELECT COUNT(c) FROM Coleta c WHERE c.status = :status")
    Long countByStatus(@Param("status") StatusColeta status);
}