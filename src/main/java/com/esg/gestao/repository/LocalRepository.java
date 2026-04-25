package com.esg.gestao.repository;

import com.esg.gestao.model.entity.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocalRepository extends JpaRepository<Local, Long> {
    Optional<Local> findByNomeLocal(String nomeLocal);
}