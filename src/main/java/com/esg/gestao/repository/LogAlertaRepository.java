package com.esg.gestao.repository;

import com.esg.gestao.model.entity.LogAlerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LogAlertaRepository extends JpaRepository<LogAlerta, Long> {
    List<LogAlerta> findByDataAlertaBetween(LocalDate inicio, LocalDate fim);
    List<LogAlerta> findTop10ByOrderByDataAlertaDesc();
}