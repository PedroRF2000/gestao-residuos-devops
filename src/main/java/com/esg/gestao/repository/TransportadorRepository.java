package com.esg.gestao.repository;

import com.esg.gestao.model.entity.Transportador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransportadorRepository extends JpaRepository<Transportador, Long> {
    Optional<Transportador> findByCnpj(String cnpj);
    List<Transportador> findByLicencaAmbientalIsNotNull();
}