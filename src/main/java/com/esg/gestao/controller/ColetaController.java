package com.esg.gestao.controller;

import com.esg.gestao.model.dto.ColetaDTO;
import com.esg.gestao.model.enums.StatusColeta;
import com.esg.gestao.service.ColetaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coletas")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-token")
@Tag(name = "Coletas", description = "Gerenciamento de coletas de resíduos")
public class ColetaController {

    private final ColetaService coletaService;

    @GetMapping
    @Operation(summary = "Listar todas as coletas", description = "Retorna lista de todas as coletas cadastradas")
    public ResponseEntity<List<ColetaDTO>> findAll() {
        return ResponseEntity.ok(coletaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar coleta por ID", description = "Retorna os dados de uma coleta específica")
    public ResponseEntity<ColetaDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(coletaService.findById(id));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Buscar coletas por status", description = "Retorna coletas filtradas por status")
    public ResponseEntity<List<ColetaDTO>> findByStatus(@PathVariable StatusColeta status) {
        return ResponseEntity.ok(coletaService.findByStatus(status));
    }

    @GetMapping("/pendentes-atrasadas")
    @Operation(summary = "Buscar coletas pendentes atrasadas",
            description = "Retorna coletas pendentes há mais de 7 dias")
    public ResponseEntity<List<ColetaDTO>> findColetasPendentesAtrasadas() {
        return ResponseEntity.ok(coletaService.findColetasPendentesAtrasadas());
    }

    @PostMapping
    @Operation(summary = "Agendar nova coleta", description = "Cria um novo agendamento de coleta")
    public ResponseEntity<ColetaDTO> create(@Valid @RequestBody ColetaDTO coletaDTO) {
        ColetaDTO created = coletaService.create(coletaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar coleta", description = "Atualiza os dados de uma coleta existente")
    public ResponseEntity<ColetaDTO> update(@PathVariable Long id, @Valid @RequestBody ColetaDTO coletaDTO) {
        return ResponseEntity.ok(coletaService.update(id, coletaDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar coleta", description = "Remove uma coleta do sistema")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        coletaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}