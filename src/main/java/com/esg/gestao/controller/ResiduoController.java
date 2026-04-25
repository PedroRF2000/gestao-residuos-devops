package com.esg.gestao.controller;

import com.esg.gestao.model.dto.ResiduoDTO;
import com.esg.gestao.service.ResiduoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/residuos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-token")
@Tag(name = "Resíduos", description = "Gerenciamento de resíduos")
public class ResiduoController {

    private final ResiduoService residuoService;

    @GetMapping
    @Operation(summary = "Listar resíduos", description = "Retorna todos os resíduos cadastrados")
    public ResponseEntity<List<ResiduoDTO>> findAll() {
        return ResponseEntity.ok(residuoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar resíduo por ID", description = "Retorna um resíduo específico")
    public ResponseEntity<ResiduoDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(residuoService.findById(id));
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Buscar resíduos por tipo", description = "Filtra resíduos pelo tipo")
    public ResponseEntity<List<ResiduoDTO>> findByTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(residuoService.findByTipo(tipo));
    }

    @GetMapping("/local/{idLocal}")
    @Operation(summary = "Buscar resíduos por local", description = "Filtra resíduos pelo local de geração")
    public ResponseEntity<List<ResiduoDTO>> findByLocalId(@PathVariable Long idLocal) {
        return ResponseEntity.ok(residuoService.findByLocalId(idLocal));
    }

    @GetMapping("/estatisticas/por-tipo")
    @Operation(summary = "Estatísticas por tipo", description = "Retorna quantidade total agrupada por tipo")
    public ResponseEntity<Map<String, BigDecimal>> getQuantidadePorTipo() {
        return ResponseEntity.ok(residuoService.getQuantidadePorTipo());
    }

    @PostMapping
    @Operation(summary = "Registrar resíduo", description = "Cadastra um novo resíduo no sistema")
    public ResponseEntity<ResiduoDTO> create(@Valid @RequestBody ResiduoDTO residuoDTO) {
        ResiduoDTO created = residuoService.create(residuoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar resíduo", description = "Atualiza dados de um resíduo")
    public ResponseEntity<ResiduoDTO> update(@PathVariable Long id, @Valid @RequestBody ResiduoDTO residuoDTO) {
        return ResponseEntity.ok(residuoService.update(id, residuoDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover resíduo", description = "Remove um resíduo do sistema")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        residuoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}