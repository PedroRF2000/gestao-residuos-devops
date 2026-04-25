package com.esg.gestao.controller;

import com.esg.gestao.model.dto.DestinoFinalDTO;
import com.esg.gestao.service.DestinoFinalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/destinos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-token")
@Tag(name = "Destinos Finais", description = "Gerenciamento de destinos finais de resíduos")
public class DestinoFinalController {

    private final DestinoFinalService destinoFinalService;

    @GetMapping
    @Operation(summary = "Listar destinos", description = "Retorna todos os destinos finais cadastrados")
    public ResponseEntity<List<DestinoFinalDTO>> findAll() {
        return ResponseEntity.ok(destinoFinalService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar destino por ID", description = "Retorna um destino final específico")
    public ResponseEntity<DestinoFinalDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(destinoFinalService.findById(id));
    }

    @GetMapping("/tipo/{tipoDestino}")
    @Operation(summary = "Buscar destinos por tipo", description = "Filtra destinos pelo tipo de destino")
    public ResponseEntity<List<DestinoFinalDTO>> findByTipo(@PathVariable String tipoDestino) {
        return ResponseEntity.ok(destinoFinalService.findByTipoDestino(tipoDestino));
    }

    @GetMapping("/coleta/{idColeta}")
    @Operation(summary = "Buscar destinos por coleta", description = "Retorna destinos de uma coleta específica")
    public ResponseEntity<List<DestinoFinalDTO>> findByColetaId(@PathVariable Long idColeta) {
        return ResponseEntity.ok(destinoFinalService.findByColetaId(idColeta));
    }

    @GetMapping("/estatisticas/por-tipo")
    @Operation(summary = "Estatísticas por tipo", description = "Retorna quantidade agrupada por tipo de destino")
    public ResponseEntity<Map<String, Long>> getQuantidadePorTipo() {
        return ResponseEntity.ok(destinoFinalService.getQuantidadePorTipoDestino());
    }

    @PostMapping
    @Operation(summary = "Registrar destino", description = "Cadastra um novo destino final")
    public ResponseEntity<DestinoFinalDTO> create(@Valid @RequestBody DestinoFinalDTO destinoDTO) {
        DestinoFinalDTO created = destinoFinalService.create(destinoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar destino", description = "Atualiza dados de um destino final")
    public ResponseEntity<DestinoFinalDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody DestinoFinalDTO destinoDTO) {
        return ResponseEntity.ok(destinoFinalService.update(id, destinoDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover destino", description = "Remove um destino final do sistema")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        destinoFinalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}